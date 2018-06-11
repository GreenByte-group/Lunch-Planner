import React from 'react';
import { StyleSheet, Text, View, WebView } from 'react-native';
import StatusBarBackground from './components/StatusBarBackground';
import firebase from 'react-native-firebase';

export default class App extends React.Component {
  render() {
    return (
      <View style={styles.container}>
          <StatusBarBackground style={{backgroundColor:'#fff'}}/>
          <WebView
              style={styles.webView}
              source={{uri: 'http://192.168.0.15:3000'}}
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
