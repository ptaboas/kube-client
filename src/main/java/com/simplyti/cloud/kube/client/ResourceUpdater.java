package com.simplyti.cloud.kube.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;

import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.PromiseCombiner;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;

public abstract class ResourceUpdater<T extends KubernetesResource,U extends Updater<T>> implements Updater<T> {
	
	private final List<JsonPatch> patches = new ArrayList<>();
	
	private final Future<T> currentFuture;
	private final EventLoop executor;
	private final PromiseCombiner resourceDependantPatchesCombiner;
	
	private boolean createdlabel = false;
	
	public ResourceUpdater(EventLoop executor,Future<T> currentFuture){
		this.currentFuture=currentFuture;
		this.executor =  executor;
		this.resourceDependantPatchesCombiner = new PromiseCombiner();
	}
	
	@SuppressWarnings("unchecked")
	public U addLabel(String name, String value) {
		onCurrentResource(current->addLabel(current,name,value));
		return (U) this;
	}
	
	protected void onCurrentResource(Consumer<T> consumer) {
		if(currentFuture.isSuccess()){
			consumer.accept(currentFuture.getNow());
		}else{
			Promise<Void> lazyPatch = executor.newPromise();
			resourceDependantPatchesCombiner.add((Future<?>)lazyPatch);
			currentFuture.addListener(future->{
				if(currentFuture.isSuccess()){
					consumer.accept(currentFuture.getNow());
					lazyPatch.setSuccess(null);
				}else{
					lazyPatch.setFailure(currentFuture.cause());
				}
			});
		}
	}
	
	private void addLabel(T resource, String name, String value) {
		if(resource.getMetadata().getLabels()==null && !createdlabel){
			addPatch(JsonPatch.add("/metadata/labels", Collections.singletonMap(name, value)));
			this.createdlabel  = true;
		}else{
			addPatch(JsonPatch.add("/metadata/labels/"+name, value));
		}
	}
	
	protected void addPatch(JsonPatch patch) {
		this.patches.add(patch);
	}
	
	@Override
	public Future<T> update() {
		Promise<T> promise = executor.newPromise();
		Promise<Void> lazyPatchesPromise = executor.newPromise();
		resourceDependantPatchesCombiner.finish(lazyPatchesPromise);
		lazyPatchesPromise.addListener(future->{
			if(future.isSuccess()){
				update(promise,patches);
			}else{
				promise.setFailure(future.cause());
			}
		});
		return promise;
	}

	protected abstract void update(Promise<T> promise,Collection<JsonPatch> patches);

}
