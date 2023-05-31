package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var usedWordsList: MutableList<String> = mutableListOf()

    private lateinit var currentWord: String

    private val _currentScrambledWord: MutableLiveData<String> = MutableLiveData()

    //  backing property concept in Kotlin
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    private val _currentWordCount: MutableLiveData<Int> = MutableLiveData(0)
    val currentWordCount: MutableLiveData<Int>
        get() = _currentWordCount

    /**
     *  val because the value of the LiveData/MutableLiveData object will remain the same,
     *  and only the data stored within the object will change
     */

    private val _score: MutableLiveData<Int> = MutableLiveData(0)
    val score: MutableLiveData<Int>
        get() = _score

    // This init block of code is run when the object instance is first created and initialized.
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    // Right before the ViewModel is destroyed, the onCleared() callback is called. This is not getting printed in the logs. Need to debug this
    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        if (usedWordsList.contains(currentWord)) {
            getNextWord()
        } else {
            // You can also do _currentWordCount.value = _currentWordCount.value?.plus(1)
            _currentWordCount.value = _currentWordCount.value?.inc()
            _currentScrambledWord.value = String(tempWord)
            usedWordsList.add(currentWord)
        }
    }

    /**
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, false)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        usedWordsList.clear()
        getNextWord()
    }
}