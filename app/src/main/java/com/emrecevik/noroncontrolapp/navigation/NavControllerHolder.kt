import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.emrecevik.noroncontrolapp.view.LoginScreen


/**
 * NavControllerHolder: NavController'ı uygulama genelinde paylaşmak için bir sınıf.
 */
class NavControllerHolder {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var _navController: NavHostController? = null

        var navController: NavHostController
            get() = _navController
                ?: throw IllegalStateException("NavController is not initialized.")
            set(value) {
                _navController = value
            }

        fun navigate(route: String) {
            navController.navigate(route) {
                popUpTo(route) { inclusive = true }
            }
        }

        fun popBackStack() {
            navController.popBackStack()
        }
    }
}
