package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private var usedWordsList: MutableList<String> = mutableListOf()

    private lateinit var currentWord: String

    private lateinit var _currentScrambledWord: String

    //  backing property concept in Kotlin
    val currentScrambledWord: String
        get() = _currentScrambledWord

    private var _currentWordCount = 0
    val currentWordCount: Int
        get() = _currentWordCount

    private var _score = 0
    val score: Int
        get() = _score

    // This init block of code is run when the object instance is first created and initialized.
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    // Right before the ViewModel is destroyed, the onCleared() callback is called.
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
            ++_currentWordCount
            _currentScrambledWord = String(tempWord)
            usedWordsList.add(currentWord)
        }
    }

    /**
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
    fun nextWord(): Boolean {
        return if (_currentWordCount < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    private fun increaseScore() {
        _score += SCORE_INCREASE
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, false)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _score = 0
        _currentWordCount = 0
        usedWordsList.clear()
        getNextWord()
    }
}