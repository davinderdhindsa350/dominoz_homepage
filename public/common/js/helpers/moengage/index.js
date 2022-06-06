import setEvents from "../events";

export const initializeMoEngage = async () => {
    try {
      if (window.Moengage === undefined) {
        (function (i, s, o, g, r, a, m, n) {
          i.moengage_object = r;
          const t = {};
          const q = function (f) {
            return function () {
              (i.moengage_q = i.moengage_q || []).push({ f: f, a: arguments });
            };
          };
          const f = [
              'track_event',
              'add_user_attribute',
              'add_first_name',
              'add_last_name',
              'add_email',
              'add_mobile',
              'add_user_name',
              'add_gender',
              'add_birthday',
              'destroy_session',
              'add_unique_user_id',
              'moe_events',
              'call_web_push',
              'track',
              'location_type_attribute'
            ],
            h = { onsite: ['getData', 'registerCallback'] };
          for (const k in f) {
            t[f[k]] = q(f[k]);
          }
          for (const k in h)
            for (const l in h[k]) {
              null == t[k] && (t[k] = {}), (t[k][h[k][l]] = q(k + '.' + h[k][l]));
            }
          a = s.createElement(o);
          m = s.getElementsByTagName(o)[0];
          a.async = 1;
          a.src = g;
          m.parentNode.insertBefore(a, m);
          i.moe =
            i.moe ||
            function () {
              n = arguments[0];
              return t;
            };
          a.onload = function () {
            if (n) {
              i[r] = moe(n);
            }
          };
        })(
          window,
          document,
          'script',
          'https://cdn.moengage.com/webpush/moe_webSdk.min.latest.js',
          'Moengage'
        );
  
        window.Moengage = moe({
          app_id: '7H8YBFR96GL05OOCI0ZP59G9',
          debug_logs: 0,  //0 for production and 1 for dev
          enableSPA: true,
          cluster: 'DC_3'
        });
      }
    } catch (err) {
      console.error('ERROR IN INITIALIZNG MOENGAGE', err);
    }
  };

  export const initializeMoEngageTime = async () => {
    initializeMoEngage();
    window.Moengage?.add_user_attribute('Language', 'English');
    window.Moengage?.add_user_attribute('UserType', 'Guest');
    setEvents("moengage", "App Open", {"eventObj":"demo"});
  };