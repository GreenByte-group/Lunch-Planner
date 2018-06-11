'use strict'
import React from 'react';
import {View, Text, StyleSheet, Platform, StatusBar} from 'react-native';

class StatusBarBackground extends React.Component{
    render(){
        return(
            <View style={[styles.statusBarBackground, this.props.style || {}]}>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    statusBarBackground: {
        height: (Platform.OS === 'ios') ? 20 : StatusBar.currentHeight,
        backgroundColor: "#fff",
    }

})

module.exports= StatusBarBackground