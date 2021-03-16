# hubitat-Sonoff-switch-DIY-mode
Hubitat Elevation driver for the Sonoff mini r2 running in DIY mode.

## Installation

- At the Hubitat Elevation (HE) device's page, click the **Add Virtual Device button**
- Fill in the **Device Name** and **Device Label** fields
- Select the **Sonoff switch DIY mode** driver a the Type field 
- - Note: it is located by the end of the list, with all other device drivers you have installed
- Click at the **Save** button

Pronto! Your device is installed. Now proceed to configure it.

## Configuration

- At the HE device's page, select the device you just created
- At the **Preferences** section of your device's page, fill in the **Sonoff switch IP address** field with, of course, your device's IP address
- Set the **Enable debug logging** option as desired
- - Note: Enabling debug logging may incur in a lot of log entries being generated - use wisely
- Click at the **Save Preferences** to save the preferences you just updated
- When you click at the **Save Preferences** button, a validadion message is displayed at the **Current States** section of the device's page (right upper corner); that message will tell if the provided IP address is a valid one (not necessarily the correct device's IP address)
- To check if the device's registered IP address corresponds to the actual one, click at the **Get Info** button at the command section of your device's page; if the **Current Status** section of your device's page is filled with the device's informations

## Commands

### Get Info

  Query the device - and display it at the **Current Status** section - all available information about the device
  
### On

  Guess what ... turn the device's switch ON !
  
### Off

  Obviously, it turn the device's swithc OFF ... great, isn't it?
  
### Refressh

  Same as **Get Info** command

## Comments

- It is desirable to use fixed IP address for your Sonoff Switch; check your router documentation on how to assign a fixed IP address for your device
