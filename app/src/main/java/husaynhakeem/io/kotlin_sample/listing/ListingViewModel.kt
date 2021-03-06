package husaynhakeem.io.kotlin_sample.listing

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.util.Log
import husaynhakeem.io.kotlin_sample.model.PeopleResult
import husaynhakeem.io.kotlin_sample.model.Person
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by husaynhakeem on 10/3/17.
 */
class ListingViewModel : ViewModel() {

    var personModel: ListingModel = ListingModel()
    var people = MutableLiveData<List<Person>>()
    val isLoading = ObservableField<Boolean>()
    val compositeDisposable = CompositeDisposable()

    fun start() {
        isLoading.set(true)
        val disposable = personModel.getPeople()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onPersonListReceived, this::onPersonListReceptionError)
        compositeDisposable.add(disposable)
    }

    private fun onPersonListReceived(peopleResult: PeopleResult) {
        isLoading.set(false)
        this.people.value = peopleResult.results
    }

    private fun onPersonListReceptionError(throwable: Throwable) {
        isLoading.set(false)
        this.people.value = null
        Log.e(ListingViewModel::class.java.simpleName, "Error while loading people data: " + throwable.message)
    }

    override public fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}