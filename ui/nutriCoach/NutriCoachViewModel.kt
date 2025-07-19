class NutriCoachViewModel : ViewModel() {
    private val fruityViceApi = FruityViceApi.create()
    private val genAiApi = GenAiApi.create()

    fun getFruitInfo(fruitName: String): LiveData<FruitInfo> {
        val result = MutableLiveData<FruitInfo>()
        viewModelScope.launch {
            val response = fruityViceApi.getFruitInfo(fruitName)
            result.postValue(response)
        }
        return result
    }

    fun getMotivationalMessage(): LiveData<String> {
        val result = MutableLiveData<String>()
        viewModelScope.launch {
            val response = genAiApi.getMotivationalMessage("Generate a short encouraging message to help someone improve their fruit intake.")
            result.postValue(response.message)
        }
        return result
    }
}
