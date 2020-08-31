package ca.jaysoo.extradimensions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.module.annotations.ReactModule;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@ReactModule(name = RNExtraDimensionsModule.MODULE_NAME)
public class RNExtraDimensionsModule extends ReactContextBaseJavaModule {

  public static final String MODULE_NAME = "RNExtraDimensions";

  public RNExtraDimensionsModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return MODULE_NAME;
  }

  // Add custom Dimensions emitter

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants =  new HashMap<>();
    final Context context = getReactApplicationContext();
    final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

    // Get the real display metrics if we are using API level 17 or higher.
    // The real metrics include system decor elements (e.g. soft menu bar).
    // See: http://developer.android.com/reference/android/view/Display.html#getRealMetrics(android.util.DisplayMetrics)
    if (Build.VERSION.SDK_INT >= 17) {
      Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
              .getDefaultDisplay();

      try {
        Display.class.getMethod("getRealMetrics", DisplayMetrics.class).invoke(display, metrics);
      } catch (Exception ignored) {
      }
    }

    constants.put("REAL_WINDOW_HEIGHT", getRealHeight(metrics));
    constants.put("REAL_WINDOW_WIDTH", getRealWidth(metrics));
    constants.put("STATUS_BAR_HEIGHT", getStatusBarHeight(metrics));
    constants.put("SOFT_MENU_BAR_HEIGHT", getSoftMenuBarHeight(metrics));
    constants.put("SMART_BAR_HEIGHT", Math.round(getSmartBarHeight(metrics)));
    constants.put("SOFT_MENU_BAR_ENABLED", hasPermanentMenuKey());

    return constants;
  }

  private boolean hasPermanentMenuKey() {
    final Context context = getReactApplicationContext();
    int showNavigationBarResId = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
    return !(showNavigationBarResId > 0 && context.getResources().getBoolean(showNavigationBarResId));
  }

  private float getStatusBarHeight(DisplayMetrics metrics) {
    final Context context = getReactApplicationContext();
    final int statusBarHeightResId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

    return statusBarHeightResId > 0
      ? Math.round(context.getResources().getDimensionPixelSize(statusBarHeightResId) / metrics.density)
      : 0;
  }

  private float getSoftMenuBarHeight(DisplayMetrics metrics) {
    if (hasPermanentMenuKey()) {
      return 0;
    }

    final Context context = getReactApplicationContext();
    final int navigationBarHeightResId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

    return navigationBarHeightResId > 0
      ? Math.round(context.getResources().getDimensionPixelSize(navigationBarHeightResId) / metrics.density)
      : 0;
  }

  private float getRealHeight(DisplayMetrics metrics) {
    return Math.round(metrics.heightPixels / metrics.density);
  }

  private float getRealWidth(DisplayMetrics metrics) {
    return Math.round(metrics.widthPixels / metrics.density);
  }

  private float getSmartBarHeight(DisplayMetrics metrics) {
    if (!Build.MANUFACTURER.equals("Meizu")) {
      return 0;
    }

    final Context context = getReactApplicationContext();
    final boolean meizuSmartBarAutoHide = Settings.System.getInt(context.getContentResolver(),
      "mz_smartbar_auto_hide", 0) == 1;

    if (meizuSmartBarAutoHide) {
      return 0;
    }

    try {
      @SuppressLint("PrivateApi") Class<?> internalDimenClass = Class.forName("com.android.internal.R$dimen");
      Field actionButtonMinHeightField = internalDimenClass.getField("mz_action_button_min_height");
      Object actionButtonMinHeight = actionButtonMinHeightField.get(internalDimenClass.newInstance());

      if (actionButtonMinHeight == null) {
        throw new Exception("mz_action_button_min_height not available");
      }

      int height = Integer.parseInt(actionButtonMinHeight.toString());
      return Math.round(context.getResources().getDimensionPixelSize(height) / metrics.density);
    } catch (Exception ignored) {
      final Resources resources = context.getResources();

      int showNavigationBarResId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
      int navigationBarHeightResId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

      if (showNavigationBarResId > 0 &&
        resources.getBoolean(showNavigationBarResId) &&
        navigationBarHeightResId > 0) {
        return Math.round(resources.getDimensionPixelSize(navigationBarHeightResId) / metrics.density);
      }

      return 0;
    }
  }
}
