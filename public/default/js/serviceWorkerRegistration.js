import { Workbox } from 'workbox-window';

export default function registerServiceWorker() {
  if ('serviceWorker' in navigator) {
    const swfilePath = (window._siteProps_.props.staticimgbaseurl).replace('/default', '')+'/sw.js'+'?v='+window.version
    const wb = new Workbox(swfilePath);
    // const wb = new Workbox('http://localhost:8080/discovery-ui-web/sw.js');
    // @todo: a Modal ui will be created for this
    const showSkipWaitingModal = (event) => {
      if (confirm(`New sw app update is available!. Click OK to refresh`)) {
        wb.addEventListener('controlling', (event) => {
          console.log(event+"accepted prompt.....will reload now");
          
          window.location.reload();
          
          console.log("reloaded browser.....Now service worker installed and UPDATED. New app update is available!");
          
        });
  
        wb.messageSkipWaiting();
      } else {
        console.log("dismissed prompt.....");
      }
    };

    // 4. Service worker activated
    wb.addEventListener('activated', (event) => {
      if (!event.isUpdate) {
        console.info('Service worker activated for the first time!');
        wb.messageSW({
          type: 'CLIENTS_CLAIM'
        });
      } else {
        console.info('Service worker activated!');
      }
    });

    // 3. service worker has installed but is waiting to activate.
    wb.addEventListener('waiting', showSkipWaitingModal);

    // 2. Service worker installed
    wb.addEventListener('installed', (event) => {
      if (!event.isUpdate) {
        console.info('Service worker installed for the first time');
      } else {
        console.info('Service worker installed');
      }
    });

    // 1. Service worker Registered

    navigator.serviceWorker.register(swfilePath, { scope: "/jfl-discovery-ui" }).then(() => {
      console.log("Install succeeded as the max allowed scope was overriden to '/jfl-discovery-ui'...");
    });
  } else {
      console.log('Service Worker is not supported in your browser, kindly update your browser for a smooth experience.');
  }

  // var defferedPrompt;
  // window.addEventListener('beforeinstallprompt', (event) => {
  //   console.log("beforeinstallprompt");
  //   event.preventDefault();
  //   defferedPrompt = event;
  //   showInstallPromotion();
  // })
}






