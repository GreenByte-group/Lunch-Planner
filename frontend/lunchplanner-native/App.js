import React from 'react';
import { StyleSheet, Text, View, WebView } from 'react-native';

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
          <WebView
              style={styles.webView}
              source={{uri: 'https://lunchplanner.greenbyte.group'}}
              avaScriptEnabled={true}
              domStorageEnabled={true}
              startInLoadingState={true}
              nativeConfig={{props: {webContentsDebuggingEnabled: true}}}
          />
      </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    webView: {
        flex: 1,
    }
});
