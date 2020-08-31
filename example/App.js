import * as React from "react";
import { Dimensions, View, Text, NativeModules } from "react-native";
// import ExtraDimensions from "react-native-extra-dimensions-android";

const window = Dimensions.get("window");

console.warn(NativeModules.RNExtraDimensions);

export const App = () => (
  <View style={{ flex: 1 }}>
    <Text>Hello</Text>

    {/* <View
      style={{
        backgroundColor: "red",
        justifyContent: "center",
        alignItems: "center",
        height: ExtraDimensions.get("STATUS_BAR_HEIGHT"),
      }}
    >
      <Text style={{ color: "white" }}>
        STATUS_BAR_HEIGHT ({ExtraDimensions.get("STATUS_BAR_HEIGHT")})
      </Text>
    </View>

    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <Text>
        REAL_WINDOW_HEIGHT ({ExtraDimensions.get("REAL_WINDOW_HEIGHT")})
      </Text>

      <Text>
        REAL_WINDOW_WIDTH ({ExtraDimensions.get("REAL_WINDOW_WIDTH")})
      </Text>
    </View>

    <View
      style={{
        backgroundColor: "blue",
        justifyContent: "center",
        alignItems: "center",
        height: ExtraDimensions.get("SOFT_MENU_BAR_HEIGHT"),
      }}
    >
      <Text style={{ color: "white" }}>
        SOFT_MENU_BAR_HEIGHT ({ExtraDimensions.get("SOFT_MENU_BAR_HEIGHT")})
      </Text>
    </View> */}
  </View>
);
