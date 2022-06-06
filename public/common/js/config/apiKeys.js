const { getPWAConfig } = require("web-vault");

const GOOGLE_MAPS_API_KEY_MAP = {
  SL: window._siteProps_?.props?.google_maps_api_key_map,
  BG: window._siteProps_?.props?.google_maps_api_key_map,
  IN: window._siteProps_?.props?.google_maps_api_key_map,
};

let DOMINO_API_KEY =
  getPWAConfig("INDIA")[window._siteProps_?.props?.build_env_var].API_KEY;

//use on local if needed
/* DOMINO_API_KEY = "1be746dc5827cf05"; */

const APP_VERSION = window._siteProps_?.props?.app_version;

export default {
  GOOGLE_MAPS_API_KEY_MAP,
  DOMINO_API_KEY,
  APP_VERSION,
};
