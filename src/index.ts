import { NativeModules, Platform } from "react-native";

export type Dimensions =
  | "REAL_WINDOW_HEIGHT"
  | "REAL_WINDOW_WIDTH"
  | "STATUS_BAR_HEIGHT"
  | "SOFT_MENU_BAR_HEIGHT"
  | "SMART_BAR_HEIGHT"
  | "SOFT_MENU_BAR_ENABLED";

// const NativeModule: {
//   get: <T>(value: Dimensions) => Promise<T>;
// } = NativeModules.ExtraDimensions;

function get(dim: Dimensions) {
  if (Platform.OS !== "android") {
    console.warn(
      "react-native-extra-dimensions-android is only available on Android. Trying to access",
      dim,
    );

    return 0;
  } else {
    // android
    try {
      if (!NativeModules.ExtraDimensions) {
        throw "ExtraDimensions not defined. Try rebuilding your project. e.g. react-native run-android";
      }

      const result = NativeModules.ExtraDimensions[dim];

      if (typeof result !== "number") {
        return result;
      }

      return result;
    } catch (e) {
      console.error(e);
    }
  }
}

export function getRealWindowHeight(): Promise<number> {
  return get("REAL_WINDOW_HEIGHT");
}

export function getRealWindowWidth(): Promise<number> {
  return get("REAL_WINDOW_WIDTH");
}

export function getStatusBarHeight(): Promise<number> {
  return get("STATUS_BAR_HEIGHT");
}

export function getSoftMenuBarHeight(): Promise<number> {
  return get("SOFT_MENU_BAR_HEIGHT");
}

export function getSmartBarHeight(): Promise<number> {
  return get("SMART_BAR_HEIGHT");
}

export function isSoftMenuBarEnabled(): Promise<boolean> {
  return get("SOFT_MENU_BAR_ENABLED");
}

// stay compatible with pre-es6 exports
export default {
  getRealWindowHeight,
  getRealWindowWidth,
  getStatusBarHeight,
  getSoftMenuBarHeight,
  getSmartBarHeight,
  isSoftMenuBarEnabled,
};
