package com.gattaca.team.keira.ui.presenter.databank;

import com.gattaca.team.keira.data.model.Model;
import com.gattaca.team.keira.domain.interactor.UseCase;
import com.gattaca.team.keira.ui.presenter.Presenter;

/**
 * Created by Robert on 18.08.2016.
 */
public class SearchByCategory implements Presenter {

    private UseCase searchDataBank;

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    private final class SearchByCategorySubscriber extends rx.Subscriber<Model> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Model model) {

        }
    }
}
