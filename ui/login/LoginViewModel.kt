class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val patientDao = AppDatabase.getDatabase(application).patientDao()

    fun claimAccount(userId: String, phoneNumber: String, name: String, password: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val patient = patientDao.findByUserIdAndPhone(userId, phoneNumber)
            if (patient != null) {
                patientDao.insert(patient.copy(name = name, password = password))
                result.postValue(true)
            } else {
                result.postValue(false)
            }
        }
        return result
    }

    fun login(userId: String, password: String): LiveData<Patient?> {
        val result = MutableLiveData<Patient?>()
        viewModelScope.launch {
            result.postValue(patientDao.validateLogin(userId, password))
        }
        return result
    }
}
