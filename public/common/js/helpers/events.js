import amplitude from 'amplitude-js';
import constant from "./constant.js";
import LocalStorageFunc from "./localStorageFunc.js"

amplitude?.getInstance().init(constant.amplitudeApiKey);

const setEvents = (type, name, eventObj) => {
	if(type == "amplitude") loadAmplitudeEvents(name, eventObj);
	else if(type == "moengage") loadMoengageEvents(name, eventObj)
}

const loadAmplitudeEvents = (event, eventProperties) => {
	eventProperties.userData = LocalStorageFunc.getUserData();
	amplitude?.getInstance().logEvent(event, eventProperties);
};

const loadMoengageEvents = (eventType, payload) => {
	if (window.Moengage !== undefined) {
		window.Moengage.track_event(eventType, payload);
	}
};

export default setEvents;