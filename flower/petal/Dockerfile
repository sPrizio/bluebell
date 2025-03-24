FROM node:23-alpine AS builder

WORKDIR /app

COPY ./flower/petal/package*.json ./flower/petal/
RUN npm install --prefix ./flower/petal

COPY ./flower/petal/ ./flower/petal/
RUN npm run build --prefix ./flower/petal

FROM node:23-alpine AS runner

WORKDIR /app

COPY --from=builder /app/flower/petal /app/flower/petal

ENV NODE_ENV=production

EXPOSE 4000

CMD ["npm", "run", "start", "--prefix", "/app/flower/petal"]