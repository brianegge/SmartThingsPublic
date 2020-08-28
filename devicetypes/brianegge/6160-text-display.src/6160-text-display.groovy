/**
 *  6160 Display
 *
 *  Copyright 2020 Brian Egge
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

metadata {
	preferences {
		section("6160 Host Information") {
			input "garHost", "string", title: "6160 Hostname or IP Address", multiple: false, required: true
			input "garPort", "number", title: "6160 Port", range: "1..65536", multiple: false, required: true
		}
	}

	definition (name: "6160 Text Display", namespace: "brianegge", author: "Brian Egge") {
		capability "Speech Synthesis"
		capability "Notification"
	}

	tiles(scale: 2) {
        // multi-line text (explicit newlines)
        standardTile("multiLine", "device.multiLine", width: 3, height: 2) {
            state "multiLine", label: "Go to settings to configure the hostname/IP and port for your 6160", defaultState: true
        }

	}
}

def parse(String description) {
	log.debug "Parsing '${description}'"

}

def speak(message) {
	log.debug "Executing 'speak'"


	try {

		def headers = [:]
		headers.put("HOST", "$garHost:$garPort")
		headers.put("Content-Type", "application/json")

		//log.debug "The Header is $headers"

		def method = "POST"

		def path = "/message"
        
        def myJson = "{ \"text\": \"${message}\",\"backlight\": \"1\" }"

		try {
			def hubAction = new physicalgraph.device.HubAction(
					[
							method: method,
							path: path,
							body: myJson,
							headers: headers
					]
			)

			log.debug hubAction
			sendHubCommand(hubAction)
		}
		catch (Exception e) {
			log.error "Hit Exception $e on $hubAction"
		}
	} catch (Exception e) {
		log.error "An error occurred while doing things: ${e}"
	}
}


def deviceNotification(message) {
	speak(message)
}

def installed() {
	sendEvent(name: "multiLine", value: "Click the settings to configure\nthe hostname/IP and port for\nyour 6160")
}