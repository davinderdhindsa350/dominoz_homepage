import { createApp } from 'vue';
// import Home from './views/Home.vue'; // @todo: this will be removed once top level components are made Golbal 
import store from '../../common/js/store';
import registerServiceWorker from './serviceWorkerRegistration';
import '../../common/js/helpers/lodash.js';
//MoEngage init
import { initializeMoEngageTime } from '../../common/js/helpers/moengage';
initializeMoEngageTime();

// Global components import
import TopBar from "../../common/js/components/TopBar.vue";
import StoreStatus from "../../common/js/components/StoreStatus.vue";
import LocationMessageBanner from "../../common/js/components/LocationMessageBanner.vue";
import LoginMessageBanner from "../../common/js/components/LoginMessageBanner.vue";
import OrderTypeTabs from "../../common/js/components/OrderTypeTabs.vue";
import SearchProducts from "../../common/js/components/SearchProducts.vue";
import ReOrder from "../../common/js/components/ReOrder.vue";
import TrackOrder from "../../common/js/components/TrackOrder.vue";
import PersonalizedOffers from "../../common/js/components/PersonalizedOffers.vue";
import CheesyRewards from "../../common/js/components/CheesyRewards.vue";
import OrderFilters from "../../common/js/components/OrderFilters.vue";
import HorizontalProductScroll from "../../common/js/components/HorizontalProductScroll.vue";
import VerticalProductScroll from "../../common/js/components/VerticalProductScroll.vue";
import HorizontalBanners from "../../common/js/components/HorizontalBanners.vue";
import YourPizzaProfile from "../../common/js/components/YourPizzaProfile.vue";
import HorizontalBannerWithText from "../../common/js/components/HorizontalBannerWithText.vue";
import PaymentOfferItem from "../../common/js/components/PaymentOfferItem.vue";
// import EDVOfferData from "../../common/js/assets/FallbackData/EDVOfferData";
import UpSell from "../../common/js/components/UpSell.vue";
import BottomBar from "../../common/js/components/BottomBar.vue";
import ScrollToTop from "../../common/js/components/ScrollToTop.vue";
import HtmlComponent from "../../common/js/components/HtmlComponent.vue";

const app = createApp({});
app.use(store);

// Global components registration 
// @todo: similarly all other components registration currently inside Home.vue will move here and Home.vue will be discarded
app.component('top-bar', TopBar);
app.component('location-message-banner', LocationMessageBanner);
app.component('store-status', StoreStatus);
app.component('login-message-banner', LoginMessageBanner);
app.component('order-type-tabs', OrderTypeTabs);
app.component('search-products', SearchProducts);
app.component('re-order', ReOrder);
app.component('track-order', TrackOrder);
app.component('personalized-offers', PersonalizedOffers);
app.component('horizontal-banner-with-text', HorizontalBannerWithText);
app.component('payment-offer-item', PaymentOfferItem);
app.component('order-filters', OrderFilters);
app.component('horizontal-product-scroll', HorizontalProductScroll);
app.component('vertical-product-scroll', VerticalProductScroll);
app.component('horizontal-banners', HorizontalBanners);
app.component('up-sell', UpSell);
app.component('your-pizza-profile', YourPizzaProfile);
app.component('cheesy-rewards', CheesyRewards);
app.component('bottom-bar', BottomBar);
app.component('scroll-to-top', ScrollToTop);
app.component('html-component', HtmlComponent);


app.mount('#themeDefault');

// @todo : uncomment this... 

registerServiceWorker();

if(typeof window != 'undefined') {
	window.onbeforeunload = () => {
		window.scrollTo(0, 0);  
	};
}
/**
 * 1. Do not download lodash.js instead import that, so it will be updated.
 * 2. inside component if you import any class; will be in TitleCase. camelCase is reserved for internal methods and variables.
 *  for exmaple
 *  (N) import localStorageFunc from "../helpers/localStorageFunc";
 *  (Y) should be import -> LocalStorageFunc from "../helpers/localStorageFunc";
 * 3. in components option: please use like this. All component will always have dash in between. ie. top-bar it can't be topbar.  also mention like below.
 *     {
 *         'top-bar':TopBar
 *     }
 *  4. There should be a key/map to send and recieve listener. for example: in case of getting and setting location:
 *      this.$store.dispatch(KeysMap.LOCATION_DETECT_GEO_LOCATION, {}); instead of this.$store.dispatch("Location/detectGeoLocationPopup", {});
 *  5. Style will be in external file only. no internal styling please.
 *  6. Coditional styling should be avoided. only fetch from the computed props or from the data property
 *  7. Key in a loop should be some_unique_string + index (product.name+index)
 *  8. Do not overuse safeVal. Error debugging won't be easy
 *  9.
 */
