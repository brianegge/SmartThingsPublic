/**
 *  Holiday Color Lights
 *
 *  Copyright 2016 ygelfand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Holiday Color Lights",
    namespace: "ygelfand",
    author: "ygelfand",
    description: "This SmartApp will change the color of selected lights based on closest holiday colors",
    category: "Convenience",
    iconUrl: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w60",
    iconX2Url: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w120",
    iconX3Url: "https://lh5.ggpht.com/xJkWtYJeUMcCz2oCc3XbN1c5xbNY87RXj3FD3yUx0k1eQ71e16WJPlq6b404Sk1qIw=w180")


preferences {
    page(name: "configurationPage")
    }
    
def configurationPage() {
	dynamicPage(name: "configurationPage", title: "Holidays setup",uninstall: true, install: true) {
		section("Lights Schedule") {
        	input "globalEnable", "bool", title: "Enabled?", defaultValue: true, required: true
            input "startTimeType", "enum", title: "Starting at", options: [["time": "A specific time"], ["sunrise": "Sunrise"], ["sunset": "Sunset"]], defaultValue: "time", submitOnChange: true
            if (startTimeType in ["sunrise","sunset"]) {
                input "startTimeOffset", "number", title: "Offset in minutes (+/-)", range: "*..*", required: false
            }
            else {
                input "starting", "time", title: "Start time", required: false
            }
            input "endTimeType", "enum", title: "Ending at", options: [["time": "A specific time"], ["sunrise": "Sunrise"], ["sunset": "Sunset"]], defaultValue: "time", submitOnChange: true
            if (endTimeType in ["sunrise","sunset"]) {
                input "endTimeOffset", "number", title: "Offset in minutes (+/-)", range: "*..*", required: false
            }
            else {
                input "ending", "time", title: "End time", required: false
            }
      		input "days", "enum", title: "Day of the week", required: true, multiple: true, options: [
            	"Sunday",
				"Monday",
				"Tuesday",
				"Wednesday",
				"Thursday",
				"Friday",
				"Saturday"
            	], defaultValue:  [
            	"Sunday",
				"Monday",
				"Tuesday",
				"Wednesday",
				"Thursday",
				"Friday",
				"Saturday" ] 
 		}
        section("Only When") {
        	input "toggleSwitch", "capability.switch", title: "Only run when this switch is on", required: false, multiple: false
            input "modes", "mode", title: "Only when mode is", required: false, multiple: true
        }
		section("Light Settings") {
            input "lights", "capability.colorControl", title: "Which Color Changing Bulbs?", multiple:true, required: true
        	input "brightnessLevel", "number", title: "Brightness Level (1-100)?", required:false, defaultValue:100, range: '1..100'
        	input "noHolidayBrightnessLevel", "number", title: "No Holiday Brightness Level (1-100)?", required:false, defaultValue:50, range: '1..100'
		}
		section("Holidays Settings") {
			input "holidays", "enum", title: "Holidays?", required: true, multiple: true, options: holidayNames(), defaultValue: holidayNames()
        	input "maxdays", "number", title: "Maximum number of days around a holiday? (-1 for unlimited)", range: '-1..60', required: true, defaultValue: -1
        	input "forceholiday", "enum", title: "Force a specific holiday?", required: false, multiple: false, options: holidayNames()
		}
		section("Frequency") {
            input "cycletime", "enum", title: "Cycle frequency?" , options: [
				[1:"1 minute"],
                [5:"5 minutes"],
				[10:"10 minutes"],
				[15:"15 minutes"],
				[30:"30 minutes"],
				[60:"1 hour"],
				[180:"3 hours"],
			], required: true, defaultValue: "10", multiple: false
			input "seperate", "enum", title: "Cycle each light individually, or all together?", required: true, multiple: false, defaultValue: "individual", options: [
				[individual:"Individual"],
				[combined:"Combined"],
			]
    		input "holidayalgo", "enum", title: "Color selection", required: true, multiple: false, defaultValue: "closest", submitOnChange: true, options: [
    			[closest:"Closest Holiday"],
            	[closestwgo:"Next Holiday (with linger)"]
    		]
        	if(holidayalgo == "closestwgo") {
            	input "lingerdays", "number", title: "Days to linger after the holiday", required: true, defaultValue: 0
        	}
		}
	}
}
def allHolidayList() {
    return [
    [name: "New Years Day", day: '01/01', colors: ['White', 'Red', 'Pink', 'Purple'] ],
    [name: "Australia Day", day: '01/26', colors: ['Philippine Green', 'Gold'] ],
    [name: "Valentine's Day", day: '02/14', colors: ["Red", "Pink", "Raspberry", "Purple", "Indigo"] ],
    [name: "Presidents Day", day: '02/20', colors: ["Red", "White", "Blue" ] ],
    [name: "St. Patrick's Day", day: '03/17', colors: ["Green", "Orange"] ],
    [name: "Easter", day: '04/12', colors: [ 'Pink', 'Turquoise', 'Aqua' ] ],
    [name: "Mothers Day", day: '05/10', colors: ['Red', 'Pink'] ],
    [name: "Memorial Day", day: '05/29', colors: ["Red", "White", "Blue" ] ],
    [name: "Fathers Day", day: '06/18', , colors: ["Blue", "Navy Blue"] ],
    [name: "Independence Day", day: '07/04', colors: ["Red", "White", "Blue" ] ],
    [name: "Labor Day", day: '09/04', colors: ["Red", "White", "Blue" ] ],
    [name: "Halloween", day: '10/31', colors: ['Orange', 'Purple' ] ],
    [name: "Veterans Day", day: '11/11', colors: ["Red", "White", "Blue" ] ],
    [name: "Thanksgiving", day: '11/23', colors: ['Orange', 'Safety Orange' ] ],
    [name: "Christmas Day", day: '12/25', colors: ["Red", "Green"] ]
	]

}
def holidayList() {
	return allHolidayList().findAll {holidays.contains(it.name)}
}

def holidayNames() {
    allHolidayList().name
}

def holidayTimestamps()  {
    def today = new Date()
	def this_year = today.format('Y')
	def last_year = (today - 365 ).format('Y')
	def next_year = (today + 365 ).format('Y')
	def timestamps = [:]
    holidayList().each {
        timestamps[Date.parse("${it.day}/${last_year} 23:59:59")] = it.name
        timestamps[Date.parse("${it.day}/${this_year} 23:59:59")] = it.name
        timestamps[Date.parse("${it.day}/${next_year} 23:59:59")] = it.name
    }
    return timestamps.sort()
}

private timeWindowStart() {
    def result = null
    if (startTimeType == "sunrise") {
        result = location.currentState("sunriseTime")?.dateValue
        if (result && startTimeOffset) {
            result = new Date(result.time + Math.round(startTimeOffset * 60000))
        }
    }
    else if (startTimeType == "sunset") {
        result = location.currentState("sunsetTime")?.dateValue
        if (result && startTimeOffset) {
            result = new Date(result.time + Math.round(startTimeOffset * 60000))
        }
    }
    else if (starting && location.timeZone) {
        result = timeToday(starting, location.timeZone)
    }
    log.trace "timeWindowStart = ${result}"
    result
}

private getSwitchOk(){
  def  result = !toggleSwitch || (toggleSwitch.currentSwitch == "on")
}
private getModeOk() {
    def result = !modes || modes.contains(location.mode)
        result
}
private getTimeOk() {
    def result = true
    def start = timeWindowStart()
    def stop = timeWindowStop()
    if (start && stop && location.timeZone) {
        result = timeOfDayIsBetween(start, stop, new Date(), location.timeZone)
    }
    def now = new Date()
    log.debug "timeOk is $result because $start < $now < $stop"
    sendNotificationEvent("timeOk = $start, $stop, $result")
    result
}
private getDaysOk() {
	def df = new java.text.SimpleDateFormat("EEEE")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	def day = df.format(new Date())
	days.contains(day)
}
private timeWindowStop() {
    def result = null
    if (endTimeType == "sunrise") {
        result = location.currentState("sunriseTime")?.dateValue
        if (result && endTimeOffset) {
            result = new Date(result.time + Math.round(endTimeOffset * 60000))
        }
       if (result < new Date()) {
           result = result + 1
       }
    }
    else if (endTimeType == "sunset") {
        result = location.currentState("sunsetTime")?.dateValue
        if (result && endTimeOffset) {
            result = new Date(result.time + Math.round(endTimeOffset * 60000))
        }
       if (result < new Date()) {
           result = result + 1
       }
    }
    else if (ending && location.timeZone) {
       def start = timeWindowStart()
       if (start > new Date()) {
           start = new Date()
       }
       result = timeTodayAfter(timeWindowStart(), ending, location.timeZone)
    }
    log.trace "timeWindowStop = ${result}"
    result
}
def closestWithoutGO(buffer=0) {
    def today = new Date()
    today = today - buffer
    def target = today.getTime()
    def last = null
    def diff = null
     holidayTimestamps().any { k, v ->
        if (k > target) {
            last = v
            diff = k
            return true
        }
        return false
    }
    if ((maxdays == -1) || ( diff < ( maxdays  * 86400000) ))
    	return last
    else
    	return null
}
def closest() {
    def today = new Date()
    def last = null
    def distance = 99999999999999
     holidayTimestamps().each { k, v ->
        def d = k - today.getTime()
        if (d.abs() < distance) {
            distance = d.abs()
            last = v
        }
    }
    if ((maxdays == -1) || ( distance < ( maxdays  * 86400000) ))
    	return last
    else
    	return null
}


def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
    state.colorOffset=0
    state.curHoliday = null
    if (brightnessLevel<1) {
		brightnessLevel=1
	}
    else if (brightnessLevel>100) {
		brightnessLevel=100
	}
    if (noHolidayBrightnessLevel<0) {
		noHolidayBrightnessLevel=0
	}
    else if (noHolidayBrightnessLevel>100) {
		noHolidayBrightnessLevel=100
	}
    setHoliday()
    if (globalEnable && getDaysOk() && getModeOk() && getTimeOk()) {
        log.debug "Resuming light show"
        lightsOn()
    } else {
		scheduleOn()
    }
    subscribe(location, "mode", modeChange)
}

def setHoliday() {
    def lastHoliday = state.curHoliday
    if(forceholiday) {
        state.curHoliday = forceholiday
    }
    else
    {
    	switch(holidayalgo) {
            case 'closest':
            	state.curHoliday = closest()
            	break;
            case 'closestwgo':
            	state.curHoliday = closestWithoutGO(lingerdays)
            	break;
 			}
    }
    log.debug "Determined current holiday is ${state.curHoliday}"    
}

def scheduleOn() {
    def start = timeWindowStart()
    if (start < new Date()) {
        start = start + 1
    }
    log.debug "Scheduling lights on at: ${start}"
    runOnce(start, lightsOn)
}

def lightsOn(evt) {
    log.debug "lightsOn"
    setHoliday()
    sendNotificationEvent("Turning holiday lights on for holiday ${state.curHoliday}")
    state.running = true
    log.debug "Turning all lights on"
    lights*.on()
    scheduleOff()
    log.debug "Scheduling change handler now"
    runIn(0, changeHandler)
}

def scheduleOff() {
	def stop = timeWindowStop()
    log.debug "Scheduling lights off at: ${stop}"
    runOnce(stop, lightsOff)
}

def lightsOff(evt) {
    log.debug "lightsOff"
    state.running = false
    lights*.off()
    unschedule(changeHandler)
    scheduleOn()
}
def modeChange(evt) {
    if (!globalEnable || !getDaysOk() || !getModeOk() || !getTimeOk()) {
        log.debug "mode changed to ${evt.value}, turning lights off"
        lightsOff()
    } else { 
        log.debug "mode changed to ${evt.value}, turning lights on"
        lightsOn()
    }
}

def shouldLightsBeOn() {
  def globalOk = globalEnable
  def daysOk = getDaysOk()
  def modeOk = getModeOk()
  def timeOk = getTimeOk()
  log.debug "shouldLightsBeOn global:${globalEnable} daysOk:${daysOk} modeOk:${modeOk} timeOk:${timeOk}"
  return globalOk && daysOk && modeOk && timeOk;
}

def changeHandler(evt) {
    log.debug "start changeHandler"
    //if (!shouldLightsBeOn()) {
    //    log.debug "Not scheduled to run yet"
    //    lightsOff()
    //    return true
    //}
    if (lights)
    {
    	def colors = []

        def brightness = brightnessLevel
        if (!state.curHoliday) {
        	log.debug "No holiday around, using default color"
            colors = ['Warm White']
            brightness = noHolidayBrightnessLevel
        } else {
            colors = allHolidayList().find {it.name == state.curHoliday }.colors
        }
		def onLights = lights.findAll { light -> light.currentSwitch.equalsIgnoreCase("on")}
        for(def light : lights) {
          log.debug "${light}: currentSwitch=${light.currentSwitch}, currentValue=${light.currentValue}, currentState=${light.currentState}"
        }
        def numberon = onLights.size();
        def numcolors = colors.size();
        log.debug "Offset: ${state.colorOffset}, numberon: ${numberon}, numcolors=${numcolors}, holiday=${state.curHoliday}"
    	if (onLights.size() > 0) {
        	if (state.colorOffset >= numcolors ) {
            	state.colorOffset = 0
            }
			if (seperate == 'combined')
				sendcolor(onLights,colors[state.colorOffset])
            else {
            	log.debug "Colors: ${colors}"
           		for(def i=0;i<numberon;i++) {
                	sendcolor(onLights[i],colors[(state.colorOffset + i) % numcolors],brightness)
                }
            }
            state.colorOffset = state.colorOffset + 1
     	}
   	}
    runIn(settings.cycletime.toInteger() * 60, changeHandler)
    log.debug "Running change handler again in ${settings.cycletime} minutes"
}

def sendcolor(lights,color,brightness)
{
	def colorPallet = [
    	"White": [ hue: 0, saturation: 0],
    	"Daylight":  [hue: 53, saturation: 91],
    	"Soft White": [hue: 23, saturation: 56],
    	"Warm White": [hue: 20, saturation: 80],
    	"Navy Blue": [hue: 61, saturation: null],
    	"Blue": [hue: 65, saturation: null ],
    	"Green": [hue: 33, saturation: null ],
        "Philippine Green": [hue: 41, saturation: null ],
    	"Turquoise": [hue: 47, saturation: null ],
    	"Aqua": [hue: 50, saturation: null],
    	"Amber": [hue: 13, saturation: null],
    	"Gold": [hue: 14, saturation: null],
    	"Yellow": [hue: 17, saturation: null],
    	"Safety Orange": [hue: 7, saturation: null],
    	"Orange": [hue: 10, saturation: null],
    	"Indigo": [hue: 73, saturation: null],
    	"Purple": [hue: 82, saturation: 100],
    	"Pink": [hue: 90.78, saturation: 67.84 ],
    	"Raspberry": [hue: 94 , saturation: null ],
    	"Red": [hue: 0, saturation: null ],
    	"Brick Red": [hue: 4, saturation: null ],
	]
	def newcolor = colorPallet."${color}"
    if(newcolor.saturation == null) newcolor.saturation = 100
    newcolor.level = brightness
    lights.each {
        if (color == "White" && it.hasCommand("coolWhiteOn"))
        {
            it.coolWhiteOn()
        } 
        else if (color == "Soft White" && it.hasCommand("softWhiteOn"))
        {
            it.softWhiteOn()
        }
        else if (color == "Warm White" && it.hasCommand("warmWhiteOn"))
        {
            it.warmWhiteOn()
        }
        else
        {
            it.setColor(newcolor)
        }
    }
    log.debug "Setting Color = ${color} for: ${lights}"

}