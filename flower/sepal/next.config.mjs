/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "images.squarespace-cdn.com",
        port: "",
      },
      {
        protocol: "https",
        hostname: "demo.themenio.com",
        port: "",
      },
      {
        protocol: "https",
        hostname: "assets.aceternity.com",
        port: "",
      },
    ],
  },
  env: {
    ENABLE_BUILD_VERSION: "true",
    SEPAL_VERSION: "0.2.6",
    ALLOW_FETCHING_MARKET_NEWS: "false",
    AUTH_ENABLED: "true",
  },
};

export default nextConfig;
