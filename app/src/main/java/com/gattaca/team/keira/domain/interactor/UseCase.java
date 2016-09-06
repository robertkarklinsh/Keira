package com.gattaca.team.keira.domain.interactor;



import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by Robert on 18.08.2016.
 */
public abstract class UseCase {

    private final Scheduler executionScheduler;
    private final Scheduler postExecutionScheduler;

    private Subscription subscription = Subscriptions.empty();

    protected UseCase(Scheduler executionScheduler, Scheduler postExecutionScheduler) {
        this.executionScheduler = executionScheduler;
        this.postExecutionScheduler = postExecutionScheduler;
    }


    protected abstract Observable buildUseCaseObservable();

    /**
     * Override this method in your UseCase.
     *
     * @param useCaseSubscriber This guy listens to observable.
     */

    @SuppressWarnings("unchecked")
    public void execute(Subscriber useCaseSubscriber) {
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(executionScheduler)
                .observeOn(postExecutionScheduler)
                .subscribe(useCaseSubscriber);
    }


    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) subscription.unsubscribe();
    }
}