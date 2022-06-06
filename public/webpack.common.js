const path = require("path");
const webpack = require("webpack");
const { VueLoaderPlugin } = require("vue-loader");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CopyPlugin = require("copy-webpack-plugin");
const { InjectManifest } = require("workbox-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const BundleAnalyzerPlugin = require("webpack-bundle-analyzer").BundleAnalyzerPlugin;
const fs = require("fs");

let themes = [
  {
    theme:{source:'default', type:'theme'}, //folder
    assets: [
      {source:["@babel/polyfill", "js/app.js"], target:"js/app.js", type:'js'},
      {source:'sass/home.scss', target:'css/home', type:'css'},
      {source:'images', target:'images', type:'copy'},
      {source:'fonts', target:'fonts', type:'copy'},
      {source:'../manifest.json', target:'../manifest.json', type:'copyManifest'}
    ]
  }
];

let themeEntries = getThemeEntries(themes);

console.log(themeEntries);

var entries = themeEntries.entries;
let env = {};

const BUILD_ENV =
  process.env.NODE_ENV != undefined ? process.env.NODE_ENV : "local";
  const chunkPathVar = (BUILD_ENV !=undefined) && (BUILD_ENV == "local" ) ? "/discovery-ui-web" : "/jfl-discovery-ui";

module.exports = {
  entry: entries,
  output: {
    path: path.resolve(__dirname, "./dist"),
    // publicPath: "./dist",
    filename: "[name]",
    // clean: false,
    publicPath: chunkPathVar + "/public/dist/",
    chunkFilename: 'default/js/[name].js?id=[chunkhash]'
  },
  resolve: {
    alias: {
      vue: "vue/dist/vue.esm-bundler.js",
    },
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        use: "vue-loader",
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: "babel-loader",
      },
      {
        test: /\.jsx$/,
        use: {
          loader: "babel-loader",
          options: {
            presets: ["@babel/preset-env"],
          },
        },
        exclude: /node_modules/,
      },
      {
        test: /\.(png|jpe?g|gif|svg)$/,
        type: "asset/resource",
        generator: {
          filename: "[name][ext]",
        },
      },
      {
        // Apply rule for fonts files
        test: /\.(woff|woff2|ttf|otf|eot)$/,
        type: "asset/resource",
        generator: {
          filename: "./default/[name][ext]",
        },
      },
      {
        test: /\.(sa|sc|c)ss$/,
        use: [
          {
            loader: MiniCssExtractPlugin.loader,
            // options: {
            //   esModule: false,
            // },
            // options: { hmr: !env.prod }
          },
          {
            loader: "css-loader",
            options: {
              url: false
            }
          },
          {
            // Then we apply postCSS fixes like autoprefixer and minifying
            loader: "postcss-loader",
          },
          {
            // First we transform SASS to standard CSS
            loader: "sass-loader",
            options: {
              implementation: require("sass"),
            },
          },
        ],
      },
      {
        test: /\.(cur|ani)$/,
        loader: "file-loader",
        options: {
          name: "[name].[ext]?[hash]",
          publicPath: "/",
        },
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new VueLoaderPlugin(),
    new MiniCssExtractPlugin({
      filename: "[name].css",
    }),
    new webpack.DefinePlugin({
      __VUE_OPTIONS_API__: "true",
      __VUE_PROD_DEVTOOLS__: "false",
      BUILD_ENV_VAR: JSON.stringify(BUILD_ENV.toLowerCase()),
      BUILD_ENV_VERSION_WEBPACK: new Date().getTime(),
    }),
    new HtmlWebpackPlugin({
      // template: './index.html',
      // filename: './index.html' //relative to root of the application
      template: path.resolve(__dirname, "index.html"),
      filename: "index.html",
    }),
    new CopyPlugin({
      patterns: themeEntries.assets
      /* [
        { from: "./default/favicon.ico", to: "" },
        { from: "./default/src/manifest.json", to: "" },
        { from: "./default/src/images/global/logos/logo_48.png", to: "logo_48.png" },
        { from: "./default/src/images/global/logos/logo_72.png", to: "logo_72.png" },
        { from: "./default/src/images/global/logos/logo_96.png", to: "logo_96.png" },
        { from: "./default/src/images/global/logos/logo_192.png", to: "logo_192.png" },
        { from: "./default/src/images/global/logos/logo_256.png", to: "logo_256.png" },
        { from: "./default/src/images/global/logos/logo_384.png", to: "logo_384.png" },
        { from: "./default/src/images/global/logos/logo_512.png", to: "logo_512.png" },
        // { from: './default/src/logo512.png', to: '' },
      ]*/,
    }),

    new InjectManifest({
      swSrc: "./src-sw.js",
      swDest: "sw.js",
      maximumFileSizeToCacheInBytes: 5*1024*1024,
      include: [
        /\.html$/,
        /\.js$/,
        /\.css$/,
        /\.woff2$/,
        /\.woff$/,
        /\.eot$/,
        /\.ttf$/,
        /\.jpg$/,
        /\.png$/,
      ],
      exclude: [/\.map$/, /manifest\.json$/, /\.(?:js.css)$/],
    }),

    {
      apply: (compiler) => {
        compiler.hooks.done.tap("everythingIsDone", (compilation) => {
          for (let i in entries) {
            let hasExnt = i.indexOf(".") > 0;
            if (!hasExnt) {
              fs.unlink(path.resolve(__dirname, "./dist/default/css") + i, function () {});
            }
          }
        });
      },
    },

    /* new BundleAnalyzerPlugin(), */
  ],
};

function getThemeEntries(allThemes) {
  //let targetDir = "public";
  for(let i=0; i<themes.length; i++) {
    let current = themes[i];
    let theme = current.theme.source;
    let assets = current.assets;
    let entries = {};
    let assetsToCopy = [];
    for(let k in assets) {
      if (assets.hasOwnProperty(k)) {
        let type = assets[k]["type"];
        let currentKeyNode = assets[k];
        switch (type) {
          case "js":
          case "css":
            if(Object.prototype.toString.call(currentKeyNode.source) === "[object Array]") {
              entries[`${theme}/${currentKeyNode.target}`] = updateMapArr(currentKeyNode.source, theme);
            } else {
              entries[`${theme}/${currentKeyNode.target}`] = `./${theme}/${currentKeyNode.source}`;
            }
            break;
          case "copy":
            assetsToCopy.push({ from: `${theme}/${currentKeyNode.source}`, to: `${theme}/${currentKeyNode.target}` });
            break;
          case "copyManifest":
            assetsToCopy.push({ from: `${theme}/${currentKeyNode.source}`, to: `${theme}/${currentKeyNode.target}` });
            break;
            
        }
      }
    }

    return {assets:assetsToCopy, entries:entries};

    function updateMapArr(arr, theme) {
      return arr.map(item=> {return (item.indexOf("@")===-1) ? `./${theme}/`+item : item});
    }

  }
}
