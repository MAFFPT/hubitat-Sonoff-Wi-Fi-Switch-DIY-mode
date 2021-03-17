/*
    Copyright 2021 Marco Felicio (maffpt@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
    in compliance with the License. You may obtain a copy of the License at:
  
        http://www.apache.org/licenses/LICENSE-2.0
  
    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
    on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
    for the specific language governing permissions and limitations under the License.
*/

import groovy.transform.Field

@Field static _Namespace = "maffpt.sonoff"
@Field static _driverVersion = "0.1.0"

@Field static _defaultPort = "8081"

@Field static _httpRequestTimeout = 5

metadata 
{
    definition (name: "Sonoff Wi-Fi Switch DIY mode", namespace: "maffpt.sonoff", author: "Marco Felicio (@maffpt)") 
    {
        capability "Switch"
        command "on"
        command "off"
        command "getInfo"
        
        capability "Refresh"
        command "refresh"
        
        attribute "preferencesValidation", "string"
    }

    preferences 
    {
        input ("switchIpAddress",
               "text",
               defaultValue: "",
               required: true,
               submitOnChange: true,
               title: "Sonoff switch IP address")
        
        input ("debugLogging",
               "bool",
               defaultValue: false,
               required: false,
               submitOnChange: true,
               title: "Enable debug logging<br /><br /><b>CAUTION:</b> a lot of log entries will be recorded!")
    }
}

def getInfo ()
{
    logDebug "getInfo: IN"
    
    def deviceData = getDeviceData ()
    
    sendEvent (name: "deviceInformation", value: deviceData)
    
    if (getDataValue ("switch") != deviceData.switch)
    {
        sendEvent (name: "switch", value: deviceData.switch)
    }
                   
    logDebug "getInfo: OUT"
}


def initialize () 
{
    logDebug "initialize: IN"
    logDebug "initialize: device.capabilities = ${device.capabilities}"
    logDebug "initialize: OUT"
}


def installed ()
{
    logDebug "installed: IN"

    initialize()

    logInfo "Sonoff switch DIY mode '${device.label}' installed - don't forget that the device's IP address must be set!"

    logDebug "installed: OUT"
}


def parse (Map jsonMap)
{
    logDebug "parse: IN"
    
    logDebug "parse: received jsonMap = ${jsonMap}"

    logDebug "parse: OUT"
}


//
//
//
def parse (String description)
{
    logDebug "parse: IN"
    
    def parsedDescription = parseLanMessage (description)
    logDebug "parse: parsed description = ${parsedDescription}"
    
    logDebug "parse: OUT"
}


//
//
//
def refresh ()
{
    logDebug "refresh: IN"
    
    getInfo ()
    
    logDebug "refresh: OUT"
}


//
//
//
def uninstalled ()
{
    logDebug "uninstalled: IN"

    logInfo "Sonoff switch DIY mode '${device.label}' successfully uninstalled"
       
    logDebug "uninstalled: OUT"
}


//
//
//
def updated () 
{
    logDebug "updated: IN"
    
    def ipAddressRegex = ~/([0-9]+\.[0-9]+\.[0-9]+\.[0-9]+)/
    def ipAddressOk = ipAddressRegex.matcher(switchIpAddress).matches()
    if (ipAddressOk)
    {
        sendEvent (name: "preferencesValidation", value: "<br />IP address (${switchIpAddress}) is valid")
        logInfo "Device's IP address is valid (${switchIpAddress})"
    }
    else
    {
        sendEvent (name: "preferencesValidation", value: "<br />IP address '${switchIpAddress}' is not valid")
        logInfo "Device's IP address is invalid (${switchIpAddress})"
    }

    logDebug "updated: OUT"
}


//
// 
//
def off ()
{
    logDebug "off: IN"

    executeAction ("off")
    
    logDebug "off: OUT"
}


//
// 
//
def on ()
{
    logDebug "on: IN"

    executeAction ("on")

    logDebug "on: OUT"
}

//
// Inner methods
//

//
// Send an action command to the device
//
def executeAction (actionToExecute)
{
    def retrySendingCommand = true
    def retryCount = 0
    def retryLimit = 3

    def uriString = "http://${switchIpAddress}:${_defaultPort}/zeroconf/switch"
    Map httpRequest = [uri: uriString, body: /{ "data": {"switch": "${actionToExecute}"}}/, contentType: "application/json", requestContentType: "application/json", timeout: _httpRequestTimeout]
    def returnData = [switch: "unrecoverable"]    
    
    logDebug "executeAction: httpRequest = ${httpRequest}"
    
    while (retrySendingCommand)
    {
        try
        {
            httpPost (httpRequest)
            {
                resp -> 
                    returnData = resp?.data
                
                logDebug "executeAction: returnData = ${returnData}"

                // If we get here, it means that the request went through fine
                // Now let's check if the returned data from the device shows that the command was executed without error
                if (returnData.error != 0)
                {
                    // Nope ... something went wrong
                    // Let's finish here, ok?
                    returnData = [switch: "unknown error (${returnData.error})"]    
                }
                else
                {
                    // Now let's reflect the switch status obtained from the device itself just to be sure and do not reflect a wrong switch event value
                    returnData = getDeviceData ()
                }
                retrySendingCommand = false
            }
        }
        catch (err)
        {
            logWarn ("executeAction: Error on httpPost = ${err}")
            if (++retryCount >= retryLimit)
            {
                retrySendingCommand = false
                logWarn ("executeAction: Exceeded the maximum number of command sending retries (${retryLimit})")
            }
            else
            {
                // Let's not overhelm the device with requests
                logDebug "executeAction: Pausing for 500 miliseconds ..."
                pauseExecution (500)
            }
        }
        sendEvent (name: "switch", value: returnData.switch)
    }
    
    if (retryCount < retryLimit)
    {
        getInfo ()
    }
    
    logDebug "executeAction: OUT"
}

               
//
// Ask the device its data
//
def getDeviceData ()
{
    def retrySendingCommand = true
    def retryCount = 0
    def retryLimit = 3

    def uriString = "http://${switchIpAddress}:${_defaultPort}/zeroconf/info"
    Map httpRequest = [uri: uriString, body: /{ "data": {}}/, contentType: "application/json", requestContentType: "application/json", timeout: _httpRequestTimeout]
    def returnData = [switch: "unrecoverable"]    
    
    logDebug "executeAction: httpRequest = ${httpRequest}"
    
    while (retrySendingCommand)
    {
        try
        {
            httpPost (httpRequest)
            {
                resp -> 
                    returnData = resp?.data?.data
                
                logDebug "executeAction: returnData = ${returnData}"
                
                retrySendingCommand = false
            }
        }
        catch (err)
        {
            logWarn ("executeAction: Error on httpPost = ${err}")
            if (++retryCount >= retryLimit)
            {
                retrySendingCommand = false
                logWarn ("executeAction: Exceeded the maximum number of command sending retries (${retryLimit})")
            }
            else
            {
                // Let's not overhelm the device with requests
                logDebug "executeAction: Pausing for 500 miliseconds ..."
                pauseExecution (500)
            }
        }
    }
    
    return returnData
}

def logDebug (message) { if (debugLogging) log.debug (message) }
def logInfo  (message) { log.info (message) }
def logWarn  (message) { log.warn (message) }
