package com.simplyti.cloud.kube.client.observe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Observable<T extends KubernetesResource> {
	
	private final EventLoop executor;
	
	private final List<Observer<T>> observers;
	
	private final AtomicReference<Channel> channelReference;

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
		observers.forEach(observer->notifyObservers(observer,event));
	}

	private void notifyObservers(Observer<T> observer, Event<T> event) {
		try{
			observer.newEvent(event);
		}catch (Throwable e) {
			log.warn("Error ocurred during kubernetes event handling",e);
		}
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

	public ChannelFuture close() {
		return channelReference.getAndSet(null).close();
	}

	public void setChannel(Channel channel,Consumer<Observable<T>> doObserve) {
		this.channelReference.set(channel);
		channel.closeFuture().addListener(close->{
			if(channelReference.get()!=null){
				doObserve.accept(this);
			}
		});
	}

	public EventLoop executor() {
		return executor;
	}
}
