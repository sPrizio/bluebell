FROM node:23-alpine AS builder

WORKDIR /app

COPY ./flower/sepal/package*.json ./flower/sepal/
RUN npm install --prefix ./flower/sepal

COPY ./flower/sepal/ ./flower/sepal/
RUN npm run build --prefix ./flower/sepal

FROM node:23-alpine AS runner

WORKDIR /app

COPY --from=builder /app/flower/sepal /app/flower/sepal

ENV NODE_ENV=production

EXPOSE 3000

CMD ["npm", "run", "start", "--prefix", "/app/flower/sepal"]