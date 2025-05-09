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
    ALLOW_FETCHING_MARKET_NEWS: "false",
  },
};

export default nextConfig;
