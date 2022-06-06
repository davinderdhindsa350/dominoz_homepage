const express = require('express');
const webpack = require('webpack');
const webpackDevMiddleware = require('webpack-dev-middleware');

const app = express();
const config = require('./webpack.dev.js'); // @todo - need to pick env basis
const compiler = webpack(config);

// Tell express to use the webpack-dev-middleware and use the webpack.config.js
// configuration file as a base.
/* app.use(
  webpackDevMiddleware(compiler, {
    publicPath: config.output.publicPath,
  })
); */

app.use('/images', express.static('src/images'));

// Serve the files on port 3000.
app.listen(4000, function () {
  console.log('Example app listening on port 3000!\n');
});

var cors_proxy = require('cors-anywhere');
cors_proxy.createServer({
    originWhitelist: [], // Allow all origins
    requireHeader: [],
    removeHeaders: []
}).listen(3002, "localhost", function() {
    console.log('Running CORS Anywhere on ' + "localhost" + ':' + 3002);
});
