package com.craftsilicon.shumul.agency.data.source.storage.pref

import android.content.Context
import android.content.SharedPreferences
import com.craftsilicon.shumul.agency.data.bean.Account
import com.craftsilicon.shumul.agency.data.bean.AccountDataTypeConverter
import com.craftsilicon.shumul.agency.data.bean.AccountOpening
import com.craftsilicon.shumul.agency.data.bean.AccountOpeningTypeConverter
import com.craftsilicon.shumul.agency.data.bean.UserData
import com.craftsilicon.shumul.agency.data.bean.UserDataTypeConverter
import com.craftsilicon.shumul.agency.data.bean.device.DeviceData
import com.craftsilicon.shumul.agency.data.bean.device.DeviceDataTypeConverter
import com.craftsilicon.shumul.agency.data.security.AppSecurity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesStorage @Inject constructor(
    private val security: AppSecurity,
    private val user: UserDataTypeConverter,
    private val deviceSerializer: DeviceDataTypeConverter,
    private val accountOpening: AccountOpeningTypeConverter,
    private val account: AccountDataTypeConverter,
    @ApplicationContext context: Context,
) :
    StorageDataSource {

    override val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)


    private val _uniqueID = MutableStateFlow(
        sharedPreferences.getString(TAG_UNIQUE_ID, "")
    )

    override val uniqueID: StateFlow<String?>
        get() = _uniqueID

    override fun setUniqueID(value: String) {
        _uniqueID.value = value
        with(sharedPreferences.edit()) {
            putString(TAG_UNIQUE_ID, value)
            apply()
        }
    }

    private val _sessionID =
        MutableStateFlow(
            sharedPreferences.getString(
                TAG_SESSION_ID,
                ""
            )
        )

    override val sessionID: StateFlow<String?>
        get() = _sessionID

    override fun sessionID(value: String) {
        _sessionID.value = value
        with(sharedPreferences.edit()) {
            putString(
                TAG_SESSION_ID,
                value
            )
            apply()
        }
    }

    override fun userData(value: UserData) {
        _userData.value = value
        with(sharedPreferences.edit()) {
            putString(
                USER_DATA,
                user.convert(value)?.let { security.encrypt(it) })
            apply()
        }
    }

    private val _userData = MutableStateFlow(
        user.convert(
            sharedPreferences.getString(USER_DATA, "")?.let {
                security.decrypt(it)
            }
        )
    )

    override val userData: StateFlow<UserData?>
        get() = _userData

    private val _login = MutableStateFlow(
        sharedPreferences.getBoolean(LOGIN_STATE, false)
    )


    override val login: StateFlow<Boolean> = _login
    override fun login(value: Boolean) {
        _login.value = value
        with(sharedPreferences.edit()) {
            putBoolean(
                LOGIN_STATE,
                value
            )
            apply()
        }
    }

    private val _deviceData = MutableStateFlow(
        deviceSerializer.convert(
            sharedPreferences.getString(TAG_DEVICE_DATA, "")?.let {
                security.decrypt(it)
            }
        )
    )
    override val deviceData: StateFlow<DeviceData?>
        get() = _deviceData


    override fun setDeviceData(value: DeviceData) {
        _deviceData.value = value
        with(sharedPreferences.edit()) {
            putString(TAG_DEVICE_DATA,
                deviceSerializer.convert(value)?.let { security.encrypt(it) })
            apply()
        }
    }

    private val _helloWorld =
        MutableStateFlow(
            sharedPreferences.getString(
                TAG_HELLO_DATA,
                ""
            )
        )

    override val helloWorld: StateFlow<String?>
        get() = _helloWorld

    override fun helloWorld(value: String) {
        _helloWorld.value = value
        with(sharedPreferences.edit()) {
            putString(
                TAG_HELLO_DATA,
                value
            )
            apply()
        }
    }

    private val _accountOpening = MutableStateFlow(
        accountOpening.convert(
            sharedPreferences.getString(TAG_ACCOUNT_OPEN_DATA, "")?.let {
                security.decrypt(it)
            }
        )
    )

    override val accountOpen: StateFlow<AccountOpening?>
        get() = _accountOpening

    override fun accountOpen(value: AccountOpening) {
        _accountOpening.value = value
        with(sharedPreferences.edit()) {
            putString(TAG_ACCOUNT_OPEN_DATA,
                accountOpening.convert(value)?.let { security.encrypt(it) })
            apply()
        }
    }

    override fun accountOpen() {
        _accountOpening.value = null
        with(sharedPreferences.edit()) {
            remove(TAG_ACCOUNT_OPEN_DATA)
            apply()
        }
    }

    private val _timeout = MutableStateFlow(
        sharedPreferences.getLong(
            TAG_TIME_OUT,
            120000
        )
    )
    override val timeout: StateFlow<Long?>
        get() = _timeout

    override fun setTimeout(value: Long?) {
        _timeout.value = value!!
        with(sharedPreferences.edit()) {
            putLong(
                TAG_TIME_OUT,
                value
            )
            apply()
        }
    }

    private val _inActivity = MutableStateFlow(
        sharedPreferences.getBoolean(
            TAG_IN_ACTIVITY,
            false
        )
    )

    override val inActivity: StateFlow<Boolean?>
        get() = _inActivity

    override fun setInactivity(value: Boolean?) {
        _inActivity.value = value!!
        with(sharedPreferences.edit()) {
            putBoolean(
                TAG_IN_ACTIVITY,
                value
            )
            apply()
        }
    }


    private val _currentAccount = MutableStateFlow(
        account.convert(
            sharedPreferences.getString(TAG_CURRENT_ACCOUNT, "")?.let {
                security.decrypt(it)
            }
        )
    )

    override fun currentAccount(value: Account) {
        with(sharedPreferences.edit()) {
            putString(TAG_CURRENT_ACCOUNT,
                account.convert(value)?.let { security.encrypt(it) })
            apply()
        }
    }

    override val currentAccount: StateFlow<Account?>
        get() = _currentAccount

    companion object {
        private const val SHARED_PREF_NAME = "pref"
        private const val USER_DATA = "userData"
        private const val LOGIN_STATE = "loginState"
        private const val TAG_UNIQUE_ID = "uniqueID"
        private const val TAG_SESSION_ID = "sessionID"
        private const val TAG_DEVICE_DATA = "deviceData"
        private const val TAG_HELLO_DATA = "helloWorld"
        private const val TAG_ACCOUNT_OPEN_DATA = "accountOpen"
        private const val TAG_TIME_OUT = "timeout"
        private const val TAG_IN_ACTIVITY = "inActivity"
        private const val TAG_CURRENT_ACCOUNT = "currentAccount"
    }
}