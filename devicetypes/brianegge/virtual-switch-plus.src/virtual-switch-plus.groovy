//Release History
//		1.0 June 21, 2021
//			Initial Release


metadata {
        definition (name: "Virtual Switch Plus", namespace: "brianegge", author: "Brian Egge") {
        capability "Switch"
        capability "Refresh"
        capability "Sensor"
        
    }

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: false,  canChangeBackground: true) {
			state "off", label: 'Away', action: "switch.on", icon: "st.Kids.kid10", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'Present', action: "switch.off", icon: "st.Kids.kid10", backgroundColor: "#53a7c0", nextState: "off"
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main (["button"])
		details(["button", "refresh"])
	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}


def on() {
	sendEvent(name: "switch", value: "on")

}

def off() {
	sendEvent(name: "switch", value: "off")

}