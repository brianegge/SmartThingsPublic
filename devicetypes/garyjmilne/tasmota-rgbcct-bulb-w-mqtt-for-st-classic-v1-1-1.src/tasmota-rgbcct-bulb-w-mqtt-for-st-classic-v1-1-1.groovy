/**
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *
 */
 
metadata {
		definition (name: "Tasmota RGBCCT Bulb w/MQTT for ST Classic V1.1.1", namespace: "garyjmilne", author: "Gary J. Milne")  {
		capability "Switch"
        capability "Switch Level"
		capability "Color Control"
		capability "Color Temperature"
		capability "Refresh"
        capability "Configuration"

		command "setAdjustedColor"
        command "setLevel"
        command "setColorTemperature"
        command "softWhiteOn"
        command "warmWhiteOn"
        command "coolWhiteOn"
        command "saveColor1"
        command "saveColor2"
        command "saveColor3"
        command "applyColor1"
        command "applyColor2"
        command "applyColor3"
        command "updateDeviceNetworkID"
        command "poll"
        command "sync"
        command "mqtt_off"
        command "mqtt_on"
        command "fade_off"
        command "fade_on"
        command "quickDim"
        command "clearSavedColors"
        command "applyBootColor"
        command "removeBootColor"
        command "reset"
		command "fadeSpeedPlus"
        command "fadeSpeedMinus"
        
        attribute "SavedColor1", "string"
        attribute "SavedColor2", "string"
        attribute "SavedColor3", "string"
        attribute "BootupColor", "string"
        attribute "Message", "string"
        attribute "FadeSpeed", "number"
	}
    
    simulator {
	}
    
    preferences {
    	section("Configure the Inputs"){
			input name: "destIp", type: "text", title: "IP", description: "The device IP", defaultValue: "192.168.0.X", required:true, displayDuringSetup: true
    		input name: "destPort", type: "test", title: "Port", description: "The webserver port.", defaultValue: "80", required:false, displayDuringSetup: true
          	input name: "username", type: "text", title: "Username", description: "Username (if configured)", required: false, displayDuringSetup: true
          	input name: "password", type: "password", title: "Password", description: "Password (if configured)", required: false, displayDuringSetup: true
            input name: "mqtt_topic", type: "text", title: "MQTT Topic", description: "Bulb Topic or Bulb Group Topic", required: false, displayDuringSetup: false
            input name: "frequency", type: "number", title: "Auto sync in X minutes", description: "Enter 1, 5, 10, 15 or 30 minutes.", defaultValue: "15", required:false, displayDuringSetup: false
            input name: "warm_white", type: "number", title: "Warm White (Kelvin)", description: "Enter preference for Soft White in Kelvin.", defaultValue: "2000", required:false, displayDuringSetup: false
            input name: "soft_white", type: "number", title: "Soft White (Kelvin)", description: "Enter preference for Warm White in Kelvin.", defaultValue: "2700", required:false, displayDuringSetup: false
            input name: "cool_white", type: "number", title: "Cool White (Kelvin)", description: "Enter preference for Cool White in Kelvin.", defaultValue: "4000", required:false, displayDuringSetup: false
            input name: "dimmer_preset1", type: "number", title: "Preset Dimmer Level 1", description: "Enter dimmer level 0-100 for preset 1.", defaultValue: "20", required:false, displayDuringSetup: false
            input name: "dimmer_preset2", type: "number", title: "Preset Dimmer Level 2", description: "Enter dimmer level 0-100 for preset 2.", defaultValue: "50", required:false, displayDuringSetup: false
           	}
	}

	tiles(scale: 2){
				multiAttributeTile(name:"switch", type: "lighting", width: 4, height: 1, canChangeIcon: true) {
                    tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                    attributeState "on", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                    attributeState "off", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
                    attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.lights.philips.hue-single", backgroundColor:"#00A0DC", nextState:"turningOff"
                    attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.lights.philips.hue-single", backgroundColor:"#ffffff", nextState:"turningOn"
				}
                tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                    attributeState "level", action:"switch level.setLevel"
                }
                tileAttribute ("device.color", key: "COLOR_CONTROL") {
                    attributeState "color", action:"setAdjustedColor"
                }
                
                tileAttribute("device.Message", key: "SECONDARY_CONTROL") {
                    attributeState("Message", label:'${currentValue}', defaultState: true)
                }
			}
        
        standardTile("warmWhite", "device.warmWhite", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
            state "off", label:"Warm White", action:"warmWhiteOn", defaultState: true, icon:"https://img.icons8.com/color/96/000000/light-off.png"
            state "on", label:"Warm White", action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-on.png"//
		}
        
        standardTile("softWhite", "device.softWhite", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
            state "off", label:'Soft White', action:"softWhiteOn", defaultState: true, icon:"https://img.icons8.com/color/96/000000/light-off.png"
            state "on", label:'Soft White', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-on.png"
		}
        
        standardTile("coolWhite", "device.coolWhite", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
			state "off", label:'Cool White', action:"coolWhiteOn", defaultState: true, icon:"https://img.icons8.com/color/96/000000/light-off.png"
            state "on", label:'Cool White', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-on.png"
		}
        
        standardTile("color1", "device.color1", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
            state "isblank", label:'Save Color 1', action:"saveColor1", defaultState: true, icon:"https://img.icons8.com/color/96/000000/settings.png"
            state "off", label:'Color 1', action:"applyColor1", defaultState: false, icon:"https://img.icons8.com/color/96/000000/color-palette.png"
        	state "on", label:'Color 1', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/rgb-lamp.png"
        }
        
        standardTile("color2", "device.color2", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
		    state "isblank", label:'Save Color 2', action:"saveColor2", defaultState: true, icon:"https://img.icons8.com/color/96/000000/settings.png"
        	state "off", label:'Color 2', action:"applyColor2", defaultState: false, icon:"https://img.icons8.com/color/96/000000/color-palette.png"
        	state "on", label:'Color 2', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/rgb-lamp.png"
		}
        
        standardTile("color3", "device.color3", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
			state "isblank", label:'Save Color 3', action:"saveColor3", defaultState: true, icon:"https://img.icons8.com/color/96/000000/settings.png"
            state "off", label:'Color 3', action:"applyColor3", defaultState: false, icon:"https://img.icons8.com/color/96/000000/color-palette.png"
        	state "on", label:'Color 3', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/rgb-lamp.png"
		}
        
        standardTile("sync", "device.sync", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight()) {
			state "idle", label:'Sync', action:"sync", defaultState: true, icon:"https://img.icons8.com/color/96/000000/replace.png"
            state "running", label:'Sync', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/synchronize--v1.png"
		}
        
        standardTile("status", "device.status", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight()) {
        	state "idle", label:'Status Idle', action:"", defaultState: true//, icon:"https://img.icons8.com/color/96/000000/sleep.png"
            state "send", label:'Sending', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/outgoing-data.png"
            state "wait", label:'Waiting', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/time.png"
            state "receive", label:'Receiving', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/incoming-data.png"
            state "success", label:'Success', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/checkmark.png"
            state "fail", label:'Fail', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/error.png"
		}
        
        controlTile("colorTemperatureControl", "device.colorTemperatureControl", "slider", width: 2, height: mintileheight(), range:"(2000..6535)") {
        	state "level", label: '${currentValue}', action:"setColorTemperature"
		}
        
        standardTile("quickdim", "quickdim", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "dim0", label: "Quick Dim", action:"quickDim", defaultState: true, icon:"https://img.icons8.com/color/96/000000/light-dimming-off.png"
            state "dim10", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-10-percent.png"
            state "dim20", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-20-percent.png"
            state "dim30", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-30-percent.png"
            state "dim40", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-40-percent.png"
            state "dim50", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-50-percent.png"
            state "dim60", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-60-percent.png"
            state "dim70", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-70-percent.png"
            state "dim80", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-80-percent.png"
            state "dim90", label: "Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-90-percent.png"
			state "dim100", label:"Quick Dim", action:"quickDim", defaultState: false, icon:"https://img.icons8.com/color/96/000000/light-dimming-100-percent.png"
		}
        
        standardTile("fade", "device.fade", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "off", label: "Fade Off", action:"fade_on", defaultState: true, icon:"https://img.icons8.com/color/96/000000/toggle-off.png", nextState:"processing"
            state "on", label: "Fade On", action:"fade_off", defaultState: false, icon:"https://img.icons8.com/color/96/000000/toggle-on.png", nextState:"processing"
            state "processing", label:'Fade <->', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/toggle-indeterminate.png"
		}
        
        standardTile("fadespeedplus", "device.fadespeedplus", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
            state "enabled", label:'Longer', action:"fadeSpeedPlus", defaultState: false, icon:"https://img.icons8.com/color/96/000000/plus-2-math.png"
            state "disabled", label:'Longer', action:"", defaultState: false, icon:"https://img.icons8.com/color/48/000000/cancel-2--v2.png"
		}
        
		valueTile("fadespeed", "device.FadeSpeed", inactiveLabel: false, decoration: "flat", width: 1, height: 2) {
			 state "Speed", label: '${currentValue}'
		}        

        standardTile("fadespeedminus", "device.fadespeedminus", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
            state "enabled", label:'Shorter', action:"fadeSpeedMinus", defaultState: false, icon:"https://img.icons8.com/color/96/000000/minus-2-math.png"
            state "disabled", label:'Shorter', action:"", defaultState: false, icon:"https://img.icons8.com/color/48/000000/cancel-2--v2.png"
		}
        
        standardTile("usemqtt", "device.usemqtt", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
			state "off", label: "Device", action:"mqtt_on", defaultState: true, icon:"https://img.icons8.com/color/96/000000/unicast.png"
            state "on", label: "MQTT", action:"mqtt_off", defaultState: false, icon:"https://img.icons8.com/color/96/000000/broadcasting.png"
		}
        
        standardTile("bootColor", "device.bootColor", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
			state "apply", label:'Boot Color', action:"applyBootColor", defaultState: false, icon:"https://img.icons8.com/color/96/000000/marker-pen.png"
            state "remove", label:'Clear Boot', action:"removeBootColor", defaultState: false, icon:"https://img.icons8.com/color/96/000000/toggle-off.png"
            state "disabled", label:'Boot Color', action:"", defaultState: false, icon:"https://img.icons8.com/color/96/000000/cancel-2--v2.png"
		}
        
        standardTile("clearSavedColors", "",inactiveLabel: false, decoration: "flat",  width: 2, height: mintileheight() ) {
			state "", label:'Clear Colors', action:"clearSavedColors", icon:"https://img.icons8.com/color/96/000000/waste.png"
       	}
        
		standardTile("wifi", "device.wifi", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
        	 state "Unknown", label: '${currentValue}', action: "", defaultState: true, icon: "https://img.icons8.com/color/96/000000/question-mark.png"
             state "No", label: '${currentValue}', action:"", icon: "https://img.icons8.com/color/96/000000/no-connection.png"
             state "Low", label: '${currentValue}', action:"", icon: "https://img.icons8.com/color/96/000000/low-connection.png"
             state "Medium", label: '${currentValue}', action:"", icon: "https://img.icons8.com/color/96/000000/medium-connection.png"
             state "High", label: '${currentValue}', action:"", icon: "https://img.icons8.com/color/96/000000/high-connection.png"     
		}
          
        standardTile("reset", "device.reset", inactiveLabel: false, decoration: "flat", width: 2, height: mintileheight() ) {
			state "", label: 'Reset UI', action:"reset", icon: "https://img.icons8.com/color/96/000000/recurring-appointment--v2.png"
		}
        
        standardTile("dni", "device.dni", inactiveLabel: false, decoration: "flat", width: 3, height: mintileheight() ) {
			state "", label:'DNI:${currentValue}', action:"updateDeviceNetworkID", defaultState: true, icon:"https://img.icons8.com/color/96/000000/ip-address.png"
       	}
        
        standardTile("icons", "device.icons", inactiveLabel: false, decoration: "flat", width: 3, height: mintileheight() ) {
			state "icon1", label:'Icon Credits: Icons8\nwww.icons8.com', action:"icons2", defaultState: false, icon:"https://img.icons8.com/color/96/000000/icons8-new-logo.png"
            //state "icon2", label:', action:"icons3", defaultState: false, icon:"https://img.icons8.com/color/96/000000/icons8-new-logo.png"
		}
        
        //These remaining tiles are never displayed.  They are effectively used as global variables which are otherwise not permitted.
        valueTile("command", "device.command", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
			 state "", label: '${currentValue}'
		}
        
        valueTile("commandvalue", "device.commandvalue", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
			state "", label: '${currentValue}'
		}
        
        valueTile("commandtime", "device.commandtime", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
			state "", label: '${currentValue}'
		}
        
        valueTile("commandflag", "device.commandflag", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
			 state "", label: '${currentValue}'
		}

		main(["switch"])
        details(["switch", "warmWhite", "softWhite", "coolWhite", "color1", "color2", "color3", "sync", "status", "quickdim", "fade", "fadespeedplus", "fadespeed", "fadespeedminus", "colorTemperatureControl", "usemqtt", "bootColor", "clearSavedColors", "wifi", "reset", "dni", "icons" ])
	}
}


//*********************************************************************************************************************************************************************
// Start of user modifiable functions
private mintileheight(){
	//Normal mode return 2
    //Dense mode return 1
	return 2
    }

//Function to selectively log activity based on varios logging levels. Normal runtime configuration is threshold = 0
//Loglevels are cumulative: -1 All errors, 0 = All user actions plus status, 1 = Entering\Exiting modules with parameters, 2 = Key variables, 3 = Extended debugging info, 4 = Extreme includes inner loops
private log(name, message, loglevel){
	
    //This is a quick way to filter out messages based on loglevel
	def threshold = 3
    if (loglevel > threshold) {return}
    
    //This is a quick way to filter out messages from a given function
    def filterlist = "listofnamestofilter: function1, anothername"
    if (filterlist.contains(name) == true) {return}

    def icon1 = " "
    def icon2 = "🏃"
    def indent = ".."
    if (loglevel == -1) icon1 = "🛑"
    if (loglevel == 0) icon1 = "0️⃣"
    if (loglevel == 1) icon1 = "1️⃣"
    if (loglevel == 2) icon1 = "2️⃣"
    if (loglevel == 3) icon1 = "3️⃣"
     
    switch(name) {                 
        case "parse":
        indent = "...."
        break;

        case "parsedevice": 
        case "parsemqtt": 
        indent = "......"
        break;

        case "checkResponse":
        indent = "........"
        icon2 = "🏁"
        break;
    }
    
    //Keyword search and icon replacement. Obviously icon2 may get overwritten so order is important.
    if (name.toString().toUpperCase().contains(" ACTION")==true ) icon2 = "🚀"
    if (name.toString().toUpperCase().contains("CALLTASMOTA")==true ) icon2 = "☎️"
    if (message.toString().toUpperCase().contains("SAVE")==true ) icon2 = "💾"
    if (message.toString().toUpperCase().contains("TURN ON")==true ) icon2 = "🟡"
    if (message.toString().toUpperCase().contains("TURN OFF")==true ) icon2 = "⚪"
    if (message.toString().toUpperCase().contains("APPLYING")==true ) icon2 = "🖍️"
    if (message.toString().toUpperCase().contains("SEND")==true ) icon2 = "⇢💡"
    if (message.toString().toUpperCase().contains("RECEIVE")==true ) icon2 = "⇠💡"
    if (message.toString().toUpperCase().contains("JSON")==true ) icon2 = "🔢"
    if (message.toString().toUpperCase().contains("SYNC")==true ) icon2 = "🔄"
    if (message.toString().toUpperCase().contains("BLOCKED")==true ) icon2 = "👎"
    if (message.toString().toUpperCase().contains("ALLOWED")==true ) icon2 = "👍"
    if (name.toString().toUpperCase().contains("POLL")==true ) icon2 = "🔂"
    if (message.toString().toUpperCase().contains("EXIT")==true ) icon2 = "💨"

    log.debug (icon2 + " " + icon1 +  "${indent}${name}: " + message)       
}

//Formats messages for display on the screen
private message(message){
	
    if (message == null) message = ""
    //Most messages that come through are the current color which has no associated text.
    def icon1 = "🎨"
    
    //Keyword search and icon replacement. Obviously icon2 may get overwritten so order is important.
    if (message.toString().toUpperCase().contains("K")==true ) icon1 = "🌡"
    if (message.toString().toUpperCase().contains("SYNC ")==true ) icon1 = "🔄"
    if (message.toString().toUpperCase().contains("SUCCESS")==true ) icon1 = "✅"
    if (message.toString().toUpperCase().contains("POLL")==true ) icon1 = "🔂"
    if (message.toString().toUpperCase().contains("WAIT")==true ) icon1 = "⏳"
    if (message.toString().toUpperCase().contains("FAIL")==true ) icon1 = "❌"
    if (message.toString().toUpperCase().contains("SYNC SUCCESS")==true ) icon1 = "📜"
    if (message.toString().toUpperCase().contains("INVALID")==true ) icon1 = "⚠️"
    if (message.toString().toUpperCase().contains("BUSY")==true ) icon1 = "⚠️"
    sendEvent(name:"Message", value: icon1 + message  )    
}

// End of user modifiable functions
//*********************************************************************************************************************************************************************


//*********************************************************************************************************************************************************************
// Start of standard functions
def installed(){
	log ("Installed with settings: ${settings}", 0)
}

def updated(){
	log ("Update", "Settings: ${settings}", 0)
	initialize()
}

def uninstalled() {
	log ("Uninstall", "Device uninstalled", 0)
}

def initialize(){
	log("Initialize", "Device initialized", 0)
    //Cancel any existing scheduled tasks for this device
    unschedule("${device}")
	//Make sure we are using the right address
    updateDeviceNetworkID()
    
    //Populate an initial time so we don't get null
    long timeStart = now()
    sendEvent(name: "commandtime", value: timeStart, displayed:false)
    
	//Test to make sure the entered frequency is in range
    switch(settings.frequency) { 
        case 1: runEvery1Minute(poll) ; break;
        case 5: runEvery5Minutes(poll) ; break;
        case 10: runEvery10Minutes(poll) ; break;
        case 15: runEvery15Minutes(poll) ; break;
        case 30: runEvery30Minutes(poll) ; break;
        default: runEvery15Minutes(poll) ; break;
    } 
   
   	//These need to be populated with initial values or they will return null as we also use their status as logic flags.
    if ( device.currentValue("status") == null ) sendEvent(name:"status", value: "idle")
	if ( device.currentValue("sync") == null ) sendEvent(name:"sync", value: "enabled")
	if ( device.currentValue("usemqtt") == null ) sendEvent(name:"usemqtt", value: "off")
    
    //Make sure we have values for first time use
    if ( device.currentValue("BootupColor") == null ) sendEvent(name:"BootupColor", value: "0" )    
	if ( device.currentValue("FadeSpeed") == null ) sendEvent(name:"FadeSpeed", value: 5) 
    
    configureDisplay("all")

}
// End of standard functions
//*********************************************************************************************************************************************************************

//*********************************************************************************************************************************************************************
// Start of functions called from UI actions

//Turns the switch on
def on() {
	log("Action", "Turn on switch", 0)
    configureDisplay("all")
	callTasmota("POWER", "On")
}

//Turns the switch off
def off() {
	log("Action", "Turn off switch", 0)
    configureDisplay("all")
    callTasmota("POWER", "Off")
}

//Enables the warmwhite tile and sets the bulb to color temp 2000
def warmWhiteOn() {
	log("Action", "Turn on warmWhite", 0)
    if ( isSystemIdle() == true ){
        configureDisplay("warmWhite")
        int kelvin = settings.warm_white
        callTasmota("CT", kelvinToMireds(2000))
        if (checkResponse() == false) configureDisplay("all") 
       }
}

//Enables the softwhite tile and sets the bulb to color temp 2700
def softWhiteOn() {
	log("Action", "Turn on softWhite", 0) 
    configureDisplay("softWhite")
    int kelvin = settings.soft_white
    callTasmota("CT", kelvinToMireds(kelvin))
}

//Enables the coolwhite tile and sets the bulb to color temp 4000
def coolWhiteOn() {
	log("Action", "Turn on coolWhite", 0)
    configureDisplay("coolWhite")
    int kelvin = settings.cool_white
    callTasmota("CT", kelvinToMireds(kelvin))
}

//Saves the currently active color to device attribute SavedColor1 and changes the state of the Color1 tile
def saveColor1() {
    log ("Action", "saveColor1", 0)
	//Make sure the system is idle and previous color selection commands have completed
    //or we risk getting a NULL saved as a color.
    def color = device.currentValue('color')
    def map = isColor(color)
    def isColor = map?.isColor
    def newcolor = map?.color
    if (isColor == true) {
        //Saving the current color to the SavedColor1 attribute for later use"
        sendEvent(name:"SavedColor1", value: newcolor)
        sendEvent(name:"color1", value: 'off', isStateChange:true)
        configureDisplay("color1") 
    }
    else message("Invalid color")
}

//Applies the color value stored in attribute SavedColor1 to the device
def applyColor1() {
	log ("Action", "applyColor1", 0)
    log ("Action", "Applying Color1: ${device.currentValue('SavedColor1')}", 1)
    configureDisplay("color1")
    callTasmota("COLOR", device.currentValue('SavedColor1'))
}

//Saves the currently active color to device attribute SavedColor2 and changes the state of the Color2 tile
def saveColor2() {
  	log ("Action", "saveColor2", 0)
    def color = device.currentValue('color')
    def map = isColor(color)
    def isColor = map?.isColor
    def newcolor = map?.color
    if (isColor == true) {
        //Saving the current color to the SavedColor2 attribute for later use"
        sendEvent(name:"SavedColor2", value: newcolor)
        sendEvent(name:"color2", value: 'off', isStateChange:true)
        configureDisplay("color2") 
    }
    else message("Invalid color")
}

//Applies the color value stored in attribute SavedColor2 to the device
def applyColor2() {
	log ("Action", "applyColor2", 0)
    log ("Action", "Applying Color2: ${device.currentValue('SavedColor2')}", 1)
    configureDisplay("color2")
    callTasmota("COLOR", device.currentValue('SavedColor2'))
}

//Saves the currently active color to device attribute SavedColor3 and changes the state of the Color3 tile
def saveColor3() {
	log ("Action", "saveColor3", 0)
    def color = device.currentValue('color')
    def map = isColor(color)
    def isColor = map?.isColor
    def newcolor = map?.color
    if (isColor == true) {
        //Saving the current color to the savedColor3 attribute for later use"
        sendEvent(name:"SavedColor3", value: newcolor)
        sendEvent(name:"color3", value: 'off', isStateChange:true)
        configureDisplay("color3") 
    }
    else message("Invalid color")
}

//Applies the color value stored in attribute SavedColor3 to the device
def applyColor3() {
	log ("Action", "applyColor3", 0)
    log ("Action", "Applying Color3: ${device.currentValue('SavedColor3')}", 1)
    configureDisplay("color3")
    callTasmota("COLOR", device.currentValue('SavedColor3'))
}

//Clears the color values stored in the attributes savedColorX
def clearSavedColors(){
	log ("Action", "Erasing saved colors", 0)	
    sendEvent(name:"SavedColor1", value: "")
    sendEvent(name:"SavedColor2", value: "")
    sendEvent(name:"SavedColor3", value: "")
    sendEvent(name:"color1", value: "isblank", isStateChange:true)
    sendEvent(name:"color2", value: "isblank", isStateChange:true)
    sendEvent(name:"color3", value: "isblank", isStateChange:true)
}

//Adjusts the dimmer to one of two preset levels which are configurable via settings
def setDimmerState(){
	def int level = device.currentValue("level")
    if ( level == null ) sendEvent(name: "quickdim", value: "dim0", isStateChange:true)
	log("setDimmerState", "Set to level: ${level}", 0)
    
      switch(level) { 
      	case 0:
        	sendEvent(name: "quickdim", value: "dim0", displayed: false, isStateChange:true)
            break
        case 1..15: 
            sendEvent(name: "quickdim", value: "dim10", displayed: false, isStateChange:true)
            break
        case 16..25: 
	        sendEvent(name: "quickdim", value: "dim20", displayed: false, isStateChange:true)
            break
        case 26..35: 
    	    sendEvent(name: "quickdim", value: "dim30", displayed: false, isStateChange:true)
            break
        case 36..45: 
        	sendEvent(name: "quickdim", value: "dim40", displayed: false, isStateChange:true)
            break
		case 46..55: 
			sendEvent(name: "quickdim", value: "dim50", displayed: false, isStateChange:true)
            break
        case 56..65: 
        	sendEvent(name:"quickdim", value: "dim60", displayed: false, isStateChange:true)
            break
        case 66..75: 
        	sendEvent(name: "quickdim", value: "dim70", displayed: false, isStateChange:true)
            break
        case 76..85: 
        	sendEvent(name: "quickdim", value: "dim80", displayed: false, isStateChange:true)
            break
        case 86..99: 
        	sendEvent(name: "quickdim", value: "dim90", displayed: false, isStateChange:true)
            break
        case 100: 
        	sendEvent(name: "quickdim", value: "dim100", displayed: false, isStateChange:true)
            break
         	}
   }
   
//Toggle the quickDim between the two different settings
def quickDim(){
	def level = device.currentValue("level")
    
	if (level == settings.dimmer_preset1) {
    	log("quickDim", "Set to Dimmer_Preset2", 0)
    	setLevel (settings.dimmer_preset2)
        }
    else {
    	log("quickDim", "Set to Dimmer_Preset1",0)
    	setLevel (settings.dimmer_preset1)
        }
    }

//Turns on fade
def fade_on() {
	log("Action", "Fade Enabled", 0)    
	if ( isSystemIdle() == true ){	
        callTasmota("FADE", "on")
        if (checkResponse() == true) sendEvent(name:"fade", value: "on", isStateChange: true)
        }
}

//Turns off fade
def fade_off() {
	log("Action", "Fade Disabled", 0)
	if ( isSystemIdle() == true ){    
        callTasmota("FADE", "off")
        if (checkResponse() == true) sendEvent(name:"fade", value: "off", isStateChange: true)
        }
}

//Increment the fadespeed value by one. This increases the length of the fade.
def fadeSpeedPlus(){
	log("Action", "fadeSpeedPlus", 0)
	//Make sure we have a value
    if ( device.currentValue("FadeSpeed") == null ) sendEvent(name:"FadeSpeed", value: 5) 
    int newfadespeed = 20
	int currentfadespeed = device.currentValue("FadeSpeed")
    log( "Action", "fadeSpeedPlus - Speed: ${currentfadespeed} + 1", 0)
    if (currentfadespeed < 20) newfadespeed = currentfadespeed + 1
    callTasmota("SPEED", newfadespeed)
		if (checkResponse() == true) {    	
        	sendEvent(name:"FadeSpeed", value: newfadespeed, isStateChange: true ) 
    	}
    }

//Decrement the fadespeed value by one. This decreases the length of the fade.
def fadeSpeedMinus(){
	log("Action", "fadeSpeedMinus", 0)
	//Make sure we have a value
    if ( device.currentValue("FadeSpeed") == null ) sendEvent(name:"FadeSpeed", value: 5) 
    int newfadespeed = 1
	int currentfadespeed = device.currentValue("FadeSpeed")
    log( "Action", "fadeSpeedPlus - Speed: ${currentfadespeed} - 1", 0)
    if (currentfadespeed > 1) newfadespeed = currentfadespeed - 1
    callTasmota("SPEED", newfadespeed)
		if (checkResponse() == true) {    	
        	sendEvent(name:"FadeSpeed", value: newfadespeed, isStateChange: true ) 
    }
}

//Configures Rule 3 on the device to force a specific color at startup
def applyBootColor() {
	def color = device.currentValue("color")
	log( "Action", "applyBootColor - Color:${color}", 0)
    
    if (device.currentValue("BootupColor") == null ) sendEvent(name:"BootupColor", value: 0, displayed:false, isStateChange: false)
	if ( isSystemIdle() == true ){
        //applyBootColor only works on a single device because Tasmota does not support multi-part parameters
		callTasmota("BACKLOG", "Rule3 on power1#boot DO color ${color} ENDON ; rule3 1")
		if (checkResponse() == true) {
            log( "applyBootColor", "BootupColor applied successfully:${color}", 1)
            sendEvent(name:"BootupColor", value: color)
			}
       	else 
        	{
       		log( "applyBootColor", "BootupColor failed to apply:${color}", 1)
            sendEvent(name:"BootupColor", value: 0)
      		}
  	}
    //Now update the Boot Color Tile appropriately.
    bootColorState()
}

//Ensures that the bootupColor tile is in the right state when it is disabled\enabled based on value within BootupColor
def bootColorState(){
	if (device.currentValue("BootupColor") == "0" ) {
    	log ("bootColorState", "Change Boot Color tile to Apply: ${device.currentValue("BootupColor")}" , 1)
    	sendEvent(name:"bootColor", value: "apply", displayed:false, isStateChange: true)
        }
    else{
    	log ("bootColorState", "Change Boot Color tile to Remove: ${device.currentValue("BootupColor")}" , 1)
    	sendEvent(name:"bootColor", value: "remove", displayed:false, isStateChange: true)
        }
	}
    
//Wipes out Rule3 on the device to remove the boot color.
def removeBootColor() {
	log("Action", "Remove Boot Color", 0)
    if ( isSystemIdle() == true ){    
        if (usemqtt() == false) {
            callTasmota("BACKLOG", /Rule3%20""/)
            }
        else {
            callTasmota("BACKLOG", /Rule3 ""/)
            }
    	if (checkResponse() == true) {
            sendEvent(name:"BootupColor", value: "0" )
		}
   	}
    //Set the tile state
    bootColorState()
}

//Reset the UI if it ever gets stuck
def reset (){
	log("Action", "Reset the UI", 0)
	//Populate an initial time so we don't get null
    long timeStart = now()
    sendEvent(name: "commandtime", value: timeStart, displayed:false, isStateChange: false)
    sendEvent(name: "status", value: "idle", isStateChange: true)
    sendEvent(name: "sync", value: "idle", isStateChange: true)
    sendEvent(name: "commandflag", value: "Complete", displayed:false, isStateChange: false)
    message(device.currentValue("color"))
    configureDisplay("all")
    }

//Configure display ensures that the tiles appear in the appropriate state at all times.
//Extra checks to minimize the number of sendEvents as these get recorded in the activity feed when isStateChange = true which is required to change the icon display.
//If we pass the parameters "all" to this function it simply turns off all of the tiles. Useful when resetting the display.
//Receives the name of a tile to place into the "on" state
def void configureDisplay(String tile){
    log("configureDisplay", "Setting correct tile state", 1)
	String tileX = ""
    ['warmWhite', 'softWhite', 'coolWhite', 'color1', 'color2', 'color3'].each{
        tileX = "${it}"
        //Force GString to regular String for comparison
        tileX = tileX.toString()
        def currentValue = device.currentValue(tileX)
        //This is the tile whose name was passed - make sure it is turned on
        if ( tileX.equals(tile) ) {
        	log("configureDisplay", "Ensuring tile ${tileX} is ON - currentValue = ${currentValue}", 2)
            if ( ( currentValue == "off" ) || ( currentValue == null ) ) {
                sendEvent(name:tileX, value: "on", displayed:false, isStateChange: true) 
                log("configureDisplay", "Tile: ${tile} has been turned ON.", 1)
            }
        }
        else {
        	log("configureDisplay", "Ensuring tile ${tileX} is OFF", 2)
        	//This is all the tiles whose names were not passed and should be in the OFF state
            if ( currentValue == "on" ) {
                sendEvent(name:tileX, value: "off", displayed:false, isStateChange: true) 
                log("configureDisplay", "Tile: ${tile} has been turned OFF.", 1)
			}
		}
	}   
}


//Sync the UI to the actual status of the device. The results come back to the parse function.
//This function is called from the button press and automatically via the polling method
def sync(){
    def mytime = elapsedTime()
    if ((device.currentValue("commandflag") == "Complete" ) || (elapsedTime() > 10)){
        log ("sync", "Sync Running..", 1)
        sendEvent(name: "sync", value: "running", displayed: false, isStateChange: true)
        message("Sync Running")
        runIn(30, syncFailed)
        callTasmota("STATE", "" )
    }
}
def syncSuccess() {
    def mytime = elapsedTime()
    log ("sync", "Sync Success...${mytime} seconds", 0)
    message("Sync Success")
    unschedule(syncFailed)
    return [createEvent(name: "sync", value: "idle") + createEvent(name: "status", value: "success")]
}

def syncFailed() {
    def mytime = elapsedTime()
    log ("sync", "Sync Failed...${mytime} seconds", -1)
    message("Sync Failed")
    sendEvent(name: "sync", value: "idle")
}

//Test to determine whether another operation is in progress. If we have two requests at the same time then there is no guarantee that they will be returned in the correct order.  Results could be problematic.
def isSystemIdle(){
	def mytime = elapsedTime()
    //If the command is flagged complete or it has been more than 10 seconds since the last command was launched 
    //then we can assume that we are no longer waiting for a response.
    log ("isSystemIdle", "Command: ${device.currentValue("command")}  Commandflag: ${device.currentValue("commandflag")}...ElapsedTime: ${mytime}", 2 )
    if ((device.currentValue("commandflag") == "Complete" ) || (elapsedTime() > 10)){
    	log ("isSystemIdle", "True - callTasmota() - Allowed", 0)
		return true
        }
   else {
   		log ("isSystemIdle", "False - callTasmota() - Blocked", 0)
   		message("System busy")
        return false
	}
}

// End of functions called from UI actions
//*********************************************************************************************************************************************************************

//*********************************************************************************************************************************************************************
// Start of MQTT related functions
//Turn on the use of MQTT. The status of the tile indicates to other functions whether to use MQTT or not.
def mqtt_on(){
	log ("Action", "Enabling MQTT", 0)
	if ( isSystemIdle() == true ){
        //See if we meet the conditions to turn on mqtt
        if (device.currentValue("usemqtt") == "off"){
            if (settings.mqtt_topic != null){            
                sendEvent(name:"usemqtt", value: "on", displayed:true)
                sendEvent(name:"bootColor", value: "disabled", displayed:false, isStateChange: true)        
                }
            }
        }
    }

def mqtt_off(){
//Turn off the use of MQTT. The status of the tile indicates to other functions whether to use MQTT or not.
	log ("Action", "Disabling MQTT", 0)
	if ( isSystemIdle() == true ){
        //We can always turn off mqtt unconditionally    
        sendEvent(name:"usemqtt", value: "off", displayed:true, isStateChange: true)
        bootColorState()
        }
    }

//Test whether MQTT is enabled
def usemqtt(){
	log ("MQTT", "usemqtt. Current command is: ${device.currentValue("command")}", 3)
    if (device.currentValue("command") == null) return "IP"
	if (device.currentValue("command") == "STATE") return "IP"
	if (device.currentValue("usemqtt") == "on") return "MQTT"
    return "IP"
    }
	
// End of MQTT related functions
//*********************************


//*********************************************************************************************************************************************************************
// Start of Background task run by Smartthings - Is executed by the polling function which syncs the state of the bulb with the UI. The bulb being considered authoritative.
//Runs on a frequency determined by the user. It will synchronize the Smartthings values to those of the actual bulb.
def poll(){
	    log ("Poll", "Polling started.. ", 0)
        sendEvent(name: "sync", value: "running")
        sync()
        sendEvent(name: "sync", value: "idle")
	}
// End of Background task
//*********************************************************************************************************************************************************************


//*********************************************************************************************************************************************************************
//Start of main program section where most of the work gets done. There are 3 functions callTasmota, parse and checkResponse
//This function places a call to the Tasmota device using HTTP via a hubCommand. A successful call will result in an HTTP response which will go to the parse() function
//Method is either IP or MQTT. We force using IP mode for reading status when the device handler is otherwise in MQTT mode.
def callTasmota(action, receivedvalue){
	def value = receivedvalue.toString()
    log ("callTasmota", "Sending command: ${action} ${value}", 0)
    
    //Update the status to show that we are sending info to the device
	sendEvent(name:"status", value: "send")

    //Capture what we are doing so we can validate whether it executed successfully or not
    //We are essentially using value tiles as global variables.
    sendEvent(name: "command", value: "${action}", displayed:false)
    sendEvent(name: "commandvalue", value: "${value}", displayed:false)
    long timeStart = now()
    sendEvent(name: "commandtime", value: timeStart, displayed:false)
    sendEvent(name: "commandflag", value: "Processing", displayed:false)
    def path
    switch(usemqtt()) { 
        case ["IP"]:
        	//Clean up the strings to make them compatible with HTML
	        value = value?.replace(" ","%20") 
    		value = value?.replace("#","%23") 
    		value = value?.replace(";","%3B") 
        	path = "/cm?user=${username}&password=${password}&cmnd=${action}%20${value}"
        	break

        case ["MQTT"]: 
        	path = "/cm?user=${username}&password=${password}&cmnd=publish%20cmnd/${settings.mqtt_topic}/${action}%20${value}"
        	break;
        }
    log ("callTasmota", "Path: ${path}", 1)
    try {
            def hubAction = new physicalgraph.device.HubAction(
                method: "GET",
                path: path,
                headers: [HOST: "${settings.destIp}:${settings.destPort}"]
                )
            log ("callTasmota", "hubaction: ${hubAction}", 3)
            sendHubCommand(hubAction)
        }
        catch (Exception e) {
            log ("calltasmota", "Exception $e in $hubAction", -1)
        }
    //If we are not confirming transaction then just call parsemqtt (same logic) and not wait for the response. Any later response will be ignored.
    def logevent = true
    log ("callTasmota","Exiting", 2)
}


//Any successful call made to the device will have a return value which come back to the parse function.
//The parse function either routes the received JSON information to parsedevice or parsemqtt for further processing.
def parse(response) {
    log ("parse", "Entering - response: ${response}", 2)
	def msg = parseLanMessage(response)
    
    log ("parse", "Response received", 0)
    def events = []
    switch(usemqtt()) { 
        case ["IP"]:                
        	log ("parse", "Calling parsedevice", 1)
	     	events = parsedevice(response)
            log ("parse", "Finished parsedevice", 1)
        	break

        case ["MQTT"]: 
        	log ("parse", "Calling parsemqtt", 1)
     		events = parsemqtt(response)
            log ("parse", "Finished parsemqtt", 1)
        	break;
        }
       log ("parse", "Exiting with ${events} events", 2)
       return events
	}

//When the device handler is in device mode the JSON response will be routed to here.
def parsedevice(response){
	def msg = parseLanMessage(response)
    //Update the status to show we have received a response
	def events = [createEvent(name:"status", value: "success")]
	
    //Get the command and value that was submitted to the callTasmota function
    def lastcommand = device.currentValue("command")
    def lastcommandvalue = device.currentValue("commandvalue")
    log("parsedevice", "lastcommand: ${lastcommand} - lastcommandvalue: ${lastcommandvalue}", 1)
    
    if (msg.json !=null){
        log ("parsedevice","JSON data is: ${msg.json}", 1)
        def taspower = msg?.json.POWER
        def tasdimmer = msg?.json.Dimmer
        def tasct = msg?.json.CT
        def tasspeed = msg?.json.Speed
        def kelvin = 0
        if (tasct != null ) { kelvin = miredsToKelvin(tasct.toInteger())}
        
        def tascolor = msg?.json.Color
        def tasfade = msg?.json.Fade
        def tasbacklog = msg?.json.WARNING
        log ("parsedevice", "Tasmota settings: Power-Fade-Backlog: ${taspower}-${tasfade}-${tasbacklog}", 2)
        if (msg.json.Wifi){
            def wifi = msg.json.Wifi
            log ("parsedevice", "Wifi: ${wifi}", 3)
            def RSSI = wifi.RSSI
            log ("parsedevice", "RSSI: ${RSSI}", 3)
            events += setWifi(RSSI)
            log ("parsedevice", "Tasmota settings: Power-Dimmer-CT-Color-Wifi: ${taspower}-${tasdimmer}-${tasct}-${tascolor}-${RSSI}", 2)
        }
        switch(lastcommand.toUpperCase()) { 
   			case ["POWER"]:
        		log("parsedevice","POWER: ${taspower}", 1)
                if (lastcommandvalue.toUpperCase() == taspower)
                	log ("parsedevice","Power state applied successfully", 0) 
                else
                    log("parsedevice","Power state failed to apply", -1)
                events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                //We got the response we were looking for so we can actually change the state of the switch in the UI.
                events += createEvent(name: "switch", value: taspower.toLowerCase(), displayed:true)
            	break
                
            case ["COLOR"]:	
                //When you adjust the Color Tasmota automatically changes the dimmer to be 100. This applies only in color mode. 
                //You can use COLOR2 to avoid this but then Tasmota changes the actual color value to complicate things.
            	def newColor = lastcommandvalue.toUpperCase()
                def map = isColor(newColor)
                def desiredColor = map.color
                log("parsedevice","Device COLOR: ${tascolor}  Desired COLOR is: ${desiredColor}", 0)
                if ( desiredColor == tascolor){
                    log ("parsedevice","Color applied successfully", 0)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                    events += createEvent(name: "switch", value: "On", displayed:true, isStateChange: true)
                    events += createEvent(name: "color", value: desiredColor, displayed:true)
                    events += createEvent(name: "level", value: 100, displayed:true, isStateChange: true)
                    message(desiredColor)
                    }
                else
                	{
                    log("parsedevice","Color state failed to apply", -1)
                	}
                break
                
            case ["DIMMER"]:
                log("parsedevice", "DIMMER: ${tasdimmer}", 1)
                if (lastcommandvalue.toLong() == tasdimmer.toLong()){
                	log ("parsedevice", "Dimmer applied successfully", 0)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                    events += createEvent(name: "level", value: lastcommandvalue, displayed:true)
                    events += createEvent(name: "switch", value: "On", displayed:true)
                } 
                else log("parsedevice","Dimmer state failed to apply", -1)
            	break
                
             case ["CT"]:
            	log("parsedevice","ColorTemperature (CT): ${tasct} mireds - ColorTemperature (CT): ${kelvin} Kelvin", 1)
                if (lastcommandvalue.toInteger() == tasct.toInteger()){
                	log ("parsedevice","Color temp applied successfully", 0)
                    events += createEvent(name: "switch", value: taspower.toLowerCase())
                    events += createEvent(name: "colorTempValue", value: kelvin, displayed:true)
                    events += createEvent(name: "colorTemperatureControl", value: kelvin, displayed:false)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                    
                    //With Tasmota, when you adjust the color temperature the color changes and the first six digits are always zero.
                    def currentcolor = tascolor
                    //Update the Smartthings color value to match the bulb.
                    log("parsedevice","Color Temp: Update color to: ${currentcolor}", 1) 
                    events += createEvent(name: "color", value: currentcolor, displayed:true)
                    message(currentColor)
                }
                else log("parsedevice","Color Temp state failed to apply", -1)
            	break
			
            case ["FADE"]:
                log("parsedevice", "In parsedevice: FADE is: ${tasfade}", 2)
                if (lastcommandvalue.toUpperCase() == "${tasfade.toUpperCase()}"){
                	log ("parsedevice","Fade applied successfully: ${tasfade.toLowerCase()}", 0)
                    events += createEvent(name:"fade", value: "${tasfade.toLowerCase()}", displayed:true, isStateChange: true)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                } 
                else log("parsedevice","Fade state failed to apply", -1)
            	break
                
            case ["SPEED"]:  //This refers to the speed of the fade. Larger numbers are longer.
                log("parsedevice", "In parsedevice: SPEED is: ${tasspeed}", 0)
                if (lastcommandvalue.toUpperCase() == "${tasspeed.toUpperCase()}"){
                	log ("parsedevice","Fade speed applied successfully: ${tasspeed.toLowerCase()}", 0)
                    events += createEvent(name:"fadespeed", value: "${tasspeed.toLowerCase()}", displayed:true, isStateChange: true)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                } 
                else log("parsedevice","Fade speed failed to apply", -1)
            	break
                
            case ["BACKLOG"]:
            	//This is the typical response to a backlog command: [WARNING:Enable weblog 2 if response expected]
                //But the bulb may be in weblog 4 and get a different response. So we just test to see if a response is present and assume it worked.
                log("parsedevice", "In parsedevice: BACKLOG command: ${tasbacklog}", 2)
                if ("${tasbacklog}" != "" ){
                	log ("parsedevice","Backlog Command applied successfully: ${tasbacklog.toLowerCase()}", 0)
                    events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                } 
                else log("parsedevice","Backlog command failed to apply", -1)
            	break
                
            case ["STATE"]:
                //Synchronise the UI to the values we get from the device
                log ("parsedevice","Setting device handler values", 1)
                events += createEvent(name: "switch", value: taspower.toLowerCase())
                events += createEvent(name: "color", value: tascolor, displayed:false)
                events += createEvent(name: "level", value: tasdimmer, displayed:false)
                events += createEvent(name: "fade", value: tasfade, displayed:false)
                log ("parsedevice","Setting Color temperature (CT):" + miredsToKelvin(tasct), 1)
                events += createEvent(name: "colorTempValue", value: miredsToKelvin(tasct), displayed:false)
               	events += createEvent(name: "commandflag", value: "Complete", displayed:false)
                events += createEvent(name: "sync", value: "enabled", displayed:false)
                events += syncSuccess()
            	break
        	}
        }
   	log ("parsedevice","Exiting", 2)
    return events
   }

//When the device handler is in MQTT mode the JSON response will be routed to here via parse()
//We do not get a full Status response with MQTT as we do with a single device, just {} or {:}
def parsemqtt(response){
	log ("parsemqtt", "Entering - Received data is: ${response}", 1)
    //Update the status to show we have received a response
	sendEvent(name:"status", value: "receive")
	def lastcommand = device.currentValue("command")
    def lastcommandvalue = device.currentValue("commandvalue")
        //So the command was send to MQTT and we got a response so we have to assume it worked.
        //We also assume that bulb is subscribed to the MQTT topic. 
        //Based on these assumptions we can make some updates to the UI
        switch(lastcommand.toUpperCase()) { 
            case ["POWER"]:
                sendEvent(name: "switch", value: lastcommandvalue, displayed:true, isStateChange: true)
                break

            case ["COLOR"]:	
                //We don't get a definitive response from the bulb (only {:}) in mqtt mode so we just assume everything worked.
                def newColor = lastcommandvalue.toUpperCase()
                def map = isColor(newColor)
                def desiredColor = map.color
                sendEvent(name: "color", value: desiredColor, displayed:true, isStateChange: true)
                message(desiredColor)
                //Level is always set to 100 when there is a color change
                sendEvent(name: "level", value: 100, displayed:true, isStateChange: true)
                sendEvent(name: "switch", value: "on", displayed:true, isStateChange: true)
                break

            case ["DIMMER"]:
                log ("parsemqtt", "Setting dimmer to: ${lastcommandvalue}", 2)
                sendEvent(name: "level", value: lastcommandvalue, displayed:true, isStateChange: true)
                break

            case ["CT"]:
                def ctvalue = lastcommandvalue.toLong()
                String kelvin = miredsToKelvin(ctvalue.toInteger())
                //We don't get a definitive response from the bulb (only {:}) in mqtt mode so we just assume everything worked.
                //As we don't get a return value for the new color we just display the requested color temp.
                sendEvent(name: "colorTemperatureControl", value: kelvin, displayed:true, isStateChange: true)
                message(kelvin +"K")
                break

            case ["FADE"]:
                sendEvent(name: "fade", value: lastcommandvalue, displayed:true, isStateChange: true)
                break
            
            case ["SPEED"]:
                sendEvent(name: "fadespeed", value: lastcommandvalue, displayed:true, isStateChange: true)
                break

            case ["STATE"]:
                //This should never execute. STATE requests all get routed to parsedevice
                break
         }
    
    //Mark the MQTT task complete
    log ("parsemqtt", "Exiting and marking command Complete", 2)
    sendEvent(name: "commandflag", value: "Complete", displayed:false)
}
 
//hubAction is an asynchronous response. Not a big deal when the device is operating normally. But when the device is offline and there is no response
//then the parse function never executes leaving us in limbo. By running checkResponse() immediately after callTasmota we can wait to see if the 
//command completed by checking on the value of commandFlag which is cleared if a response is received.
//If a response is not received within 10 seconds then checkResponse will time-out. A 
def checkResponse(){
	log("checkResponse", "Entering.", 2)
	//Max loop of 20 iterations of 0.5 second each for the 10 second HTTP timeout.
    def x = 0
    def wait = 0
	for (x = 10; x >= 0; x--) {
    	sleepForDuration(1000)
        log("checkResponse", "Processing loop: ${x}", 3)
        log("checkResponse", "CommandFlag is: ${device.currentValue("commandflag")}", 3)
        //If the command has completed we will exit early
        if (device.currentValue("commandflag") == "Complete"){
            //Exit the For loop
            break
        }
        else
        {
        	//Update the status to show we are waiting for a response
            wait = Math.round(x/2)
            //Update the status to show we have received a response. Only do an update on the first change to wait to avoid flooding logs
            if (device.currentValue("status") != "wait") sendEvent(name:"status", value: "wait", displayed:true, isStateChange:true)
        }
    }
    //If we get this far it means that either the command was complete or it is still Processing\timed out.
    def mytime = elapsedTime()
    def boolean result = false
    log("checkResponse", "CommandFlag is: ${device.currentValue("commandflag")}", 1)
    switch(device.currentValue("commandflag")) { 
        case ["Complete"]: 
        log("checkResponse", "Success...${mytime} seconds" , 0)
        //Update the status to show we have received a response
        sendEvent(name:"status", value: "success", displayed:true, isStateChange:true)
        result = true
        break

        case ["Processing"]: 
        log("checkResponse", "Fail-Timeout" , -1)
        //After 10 seconds of processing we can consider the request timed out\failed.
        sendEvent(name: "commandflag", value: "Failed...${mytime} seconds", displayed:false)
        //Update the status to show we have NOT received a response
        sendEvent(name:"status", value: "fail", displayed:true, isStateChange:true)
        result = false
        break;
    }
    log("checkResponse", "Exiting with: ${result}" , 1)
    return result
}

//End of main program section
//*********************************************************************************************************************************************************************


//*********************************************************************************************************************************************************************
//Start of Color related functions - Copy of Smartthings RGB functions with adjustments for calls to Tasmota
//This function is called directly by the color temp slider
def setColorTemperature(ct){
    log("Action", "Request Color Temp: ${ct}" , 1)
    configureDisplay("all")
    def colortemp = kelvinToMireds(ct)
    callTasmota("CT", colortemp )
}

//This function is called directly by the dimmer slider
def setLevel(percent) {
    log ("Action", "Request Dimmer: ${percent}%", 1)
    //configureDisplay("all")
    callTasmota("Dimmer", percent)
}

//This function is called directly by the color picker which provides a HEX color.
//It also supports HSV for compatibility with other platforms such as SharpTools
def setColor(value) {
	def desiredColor
	log("Action", "Request Color: ${value}", 0)
    
    def valuehex = value?.hex
    def valuehue = value?.hue
    def valuesat = value?.saturation
    
    if (valuehex != null){
    	//We can just treat this as a hex color
    	log ("setColor", "Requested Hex color: ${valuehex}", 1)
    	def map = isColor(valuehex)
    	desiredColor = map.color
        }
    
    if ((valuehex == null) && (valuehue != null) && (valuesat != null)){
    	//It must be an HSL color
        log ("setColor", "Requested HSL - H:${valuehue} S:${valuesat}", 1)
        
        String hexcolor = colorUtil.hsvToHex(Math.round(valuehue), Math.round(valuesat))
        desiredColor = hexcolor
        //This is going to appear to Tasmota as a color change and Tasmota will respond with setting the dimmer at 100.
        //This change will be reflected automatically in the SmartThings app but may not be picked up by other integration platforms if that was the source of the color selection.
        message(desiredColor)
        }
    
    //Now we finally set the color accordingly.
    if ( isSystemIdle() == true ){
        configureDisplay("all")
    	callTasmota("COLOR", desiredColor )   
        message(desiredColor)
    }
}


//This function is called directly by the up\down controls on the slider
def nextLevel() {
	def level = device.latestValue("level") as Integer ?: 0
	if (level <= 100) {
		level = Math.min(25 * (Math.round(level / 25) + 1), 100) as Integer
	}
	else {
		level = 25
	}
	setLevel(level)
}

//SmartThings function whose purpose is not entirely clear to me.
def setAdjustedColor(value) {
	if (value) {
    	log("setAdjustedColor", "Set Adjusted Color to: ${value}", 3)
		def adjusted = value + [:]
		adjusted.hue = adjustOutgoingHue(value.hue)
		// Needed because color picker always sends 100
		adjusted.level = null
		setColor(adjusted)
	}
}

//SmartThings function whose purpose is not entirely clear to me.
def adjustOutgoingHue(percent) {
	def adjusted = percent
	if (percent > 31) {
		if (percent < 63.0) {
			adjusted = percent + (7 * (percent -30 ) / 32)
		}
		else if (percent < 73.0) {
			adjusted = 69 + (5 * (percent - 62) / 10)
		}
		else {
			adjusted = percent + (2 * (100 - percent) / 28)
		}
	}
}

//Tests whether a given color is RGB or W and returns true or false plus the cleaned up color
def isColor(colorIn){
	String Color = colorIn.toString()
    if (Color.substring(0, 1) == "#"){
        Color = Color.substring(1)
        }
    //Add trailing 0's if needed
    if (Color.length() == 6){
    	log ("isColor", "Length: 6 - Color:${Color}", 2) 
        Color = Color + "0000"
        }
    else {
    	log ("isColor", "Length: <6> - Color:${Color}", 2)
        }
        
    if ( Color.startsWith("000000") == true ){
    	log ("isColor", "False - ${Color}", 1)
        return [isColor: false, color: Color]
    	}	
    else {
    	log ("isColor", "True - ${Color}", 1)
    	return [isColor: true, color: Color]
        }
}

// End of Color related functions
//*********************************************************************************************************************************************************************


//*********************************************************************************************************************************************************************
// Start of Supporting functions
private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02X', it.toInteger() ) }.join()
    return hex
}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04X', port.toInteger() )
    return hexport
}

//Updates the device network information - Allows user to force an update of the device network information if required.
def updateDeviceNetworkID() {
	def newline = "\n"
    def tab = "\t"
    
    try{
    	log("updateDeviceNetworkID", "Settings are:" + settings.destIp + ":" + settings.destPort, 1)
        def hosthex = convertIPtoHex(settings.destIp)
        def porthex = convertPortToHex(settings.destPort)
    	def desireddni = "$hosthex:$porthex"
        
        def actualdni = device.deviceNetworkId
        
        //If they don't match then we need to update the DNI
        if (desireddni !=  actualdni){
        	device.deviceNetworkId = "$hosthex:$porthex" 
            log("Action", "Save updated DNI: ${"$hosthex:$porthex"}", 0)
         	}
        else
        	{
            log("Action", "DNI: ${"$hosthex:$porthex"} is correct. Not updated. ", 0)
            }
        sendEvent(name: "dni", value: device.deviceNetworkId)
        }
    catch (e){
    	log("Save", "Error updating Device Network ID: ${e}", -1)
     	}
}

//Sleeps the process for duration seconds in a non blocking manner.
//Used to wait after a hubAction request to pause the thread execution and see if there is any response.
def sleepForDuration(msDuration)
{
	def dTotalSleep = 0
	def dStart = new Date().getTime()
    def cmds = []
	cmds << "delay 1"   
    while (dTotalSleep <= msDuration)
    {            
		cmds
        dTotalSleep = (new Date().getTime() - dStart)
    }
    log("sleepForDuration", "Slept ${dTotalSleep}ms", 3)
}

//Returns the elapsed time since the command was originally issued
def elapsedTime(){
    def strlastcommandtime = device.currentValue("commandtime")
    long lastcommandtime = Long.parseLong(strlastcommandtime)
    long currenttime = now()
    def timedifference = (currenttime - lastcommandtime)/1000
    return timedifference
}

//Tasmota CT is defined in Mireds
def int miredsToKelvin(mireds){
	mireds = mireds.toInteger()
    if (mireds < 153) mireds = 153
    if (mireds > 500) mireds = 500
	def float kelvinfloat = 1000000/mireds
    def int kelvin = kelvinfloat.toInteger()
	return kelvin
    }

//Tasmota only recognizes Mireds in a range from 153-500. Values outside that range are ignored.
def int kelvinToMireds(kelvin){
	kelvin = kelvin.toInteger()
    def float miredsfloat = 1000000/kelvin
    def int mireds = miredsfloat.toInteger()
    if (mireds < 153) return 153
    if (mireds > 500) return 500
	return mireds
}

//Selects the state of the Wifi tile based on the Wifi signal strength information received from Tasmota
private setWifi(signal){
	log("Wifi", "Signal is ${signal}", 2)
    int RSSI = signal.toInteger()
    log("Wifi", "RSSI is ${RSSI}", 2)
    def event = null
	  switch(RSSI) { 
      		case 101:                
            	//Special case we can call when needed when we don't know the Wifi state.
                event = createEvent(name:"wifi", value: "Unknown")
                break
            
			case 75..100:                
                event = createEvent(name:"wifi", value: "High")
                break

            case 45..74: 
                event = createEvent(name:"wifi", value: "Medium")
                break

            case 1..44: 
                event = createEvent(name:"wifi", value: "Low")
                break

            case 0: 
                event = createEvent(name:"wifi", value: "None")
                break;
       }
    return event
}

// End of Supporting functions
//*********************************************************************************************************************************************************************



//*********************************************************************************************************************************************************************
//Start of Future development section - no calls are made to these functions at this time

    	
//End of Future development section
//*********************************************************************************************************************************************************************