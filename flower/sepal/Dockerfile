FROM node:23-alpine AS builder

WORKDIR /app

COPY sepal/package*.json ./
RUN npm install

COPY sepal/ ./
RUN npm run build

FROM node:23-alpine AS runner

WORKDIR /app

COPY --from=builder /app ./

ENV NODE_ENV=production

EXPOSE 3000

CMD ["npm", "run", "start"]