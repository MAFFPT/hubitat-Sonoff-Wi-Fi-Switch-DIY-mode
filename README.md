# Sonoff Switch in DIY-mode support for Hubitat Elevation
Hubitat Elevation driver for the Sonoff mini r2 running in DIY mode.

## Prerequisites

Besides a device, of course ...

- DIY mode

Set your device in the DIY (Do It Yourself) mode in order to use this driver.

Check this link [Setting up Sonoff DIY mode](https://github.com/itead/Sonoff_Devices_DIY_Tools/blob/master/SONOFF%20DIY%20MODE%20Protocol%20Doc%20v1.4.md) to set your device in DIY mode.

## Driver installation

You have two options to install this driver at your Hubitat Elevation hub:

- Automatically, using the awsome @dman2306's Hubitat Package Manager (HPM)

Using HPM, select the **Install** option at HPM main page, then, using the **Search by Keywords**, search for "sonoff diy". It should show the

Check the HPM documentation on how to install HPM in your hub.

This is the preferable option.

- Manually

Follow Hubitat's instructions on how to install manually a device driver by clicking [here](https://docs.hubitat.com/index.php?title=How_to_Install_Custom_Drivers#:~:text=From%20the%20home%20page%20of,opens%20the%20Edit%20Device%20view.)



- At the Hubitat Elevation (HE) device's page, click the **Add Virtual Device button**
- Fill in the **Device Name** and **Device Label** fields
- Select the **Sonoff switch DIY mode** driver a the Type field 
  - Note: it is located by the end of the list, with all other device drivers you have installed
- Click at the **Save** button

Pronto! Your device is installed. Now proceed to configure it.

## Configuration

- At the HE device's page, select the device you just created
- At the **Preferences** section of your device's page, fill in the **Sonoff switch IP address** field with, of course, your device's IP address
- Set the **Enable debug logging** option as desired
  - Note: Enabling debug logging may incur in a lot of log entries being generated - use wisely
- Click at the **Save Preferences** to save the preferences you just updated
- When you click at the **Save Preferences** button, a validadion message is displayed at the **Current States** section of the device's page (right upper corner); this message will tell if the provided IP address is a valid one (not necessarily the correct device's IP address)
- To check if the device's registered IP address corresponds to the actual one, click at the **Get Info** button at the command section of your device's page; if the **Current Status** section of your device's page is filled with the device's informations

## Commands

### Get Info

Query the device and obtain all available informations about it and display those informations at the **Current Status** section.
  
### On

Guess what ... turn the device's switch ON!
  
### Off

Obviously, it turn the device's swithc OFF ... great, isn't it?
  
### Refressh

Same as **Get Info** command

## Comments

- Fixed IP address
 
It is desirable to use fixed IP address for your Sonoff Switch; check your router documentation on how to assign a fixed IP address for your device.

- Including support for other Sonoff Switches

If you want to include support for other Sonoff switches, fell free to fork this repository in order to do it. However, I'd be glad if you create a pull request to allow me to include your changes to this main branch - it will allow to this driver to grow as new contributions are made. It's up to you! 
