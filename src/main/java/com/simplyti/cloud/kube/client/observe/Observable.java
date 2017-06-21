package com.simplyti.cloud.kube.client.observe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import lombok.Getter;

public class Observable<T extends KubernetesResource> {
	
	private final EventLoop executor;
	
	private final List<Observer<T>> observers;
	
	private final AtomicReference<Channel> channelReference;

	@Getter
	private String index;
	
	public Observable(EventLoop executor,String index){
		this.executor=executor;
		this.index=index;
		this.observers = new ArrayList<>();
		this.channelReference=new AtomicReference<Channel>();
	}

	public void notifyObservers(Event<T> event) {
		if(executor.inEventLoop()){
			notifyObservers0(event);
		}else{
			executor.submit(()->notifyObservers0(event));
		}
	}

	private void notifyObservers0(Event<T> event) {
		this.index=event.getObject().getMetadata().getResourceVersion();
		observers.forEach(observer->observer.newEvent(event));
	}

	public Observable<T> onEvent(Observer<T> observer) {
		if(executor.inEventLoop()){
			addObserver(observer);
		}else{
			executor.submit(()->addObserver(observer));
		}
		return this;
	}

	private void addObserver(Observer<T> observer) {
		observers.add(observer);
	}

	public String index(){
		return this.index;
	}

	public void close() {
		channelReference.getAndSet(null).close();
	}

	public void setChannel(Channel channel,Consumer<Observable<T>> doObserve) {
		this.channelReference.set(channel);
		channel.closeFuture().addListener(close->{
			if(channelReference.get()!=null){
				doObserve.accept(this);
			}
		});
	}
}
