FROM node:23-alpine AS builder

ARG FLOWER_PROFILE

WORKDIR /app

COPY ./flower/sepal/package*.json ./flower/sepal/
RUN npm install --prefix ./flower/sepal

COPY ./flower/sepal/ ./flower/sepal/
RUN node ./flower/sepal/scripts/init-env.js "$FLOWER_PROFILE"

RUN echo "Using FLOWER_PROFILE=$FLOWER_PROFILE"
RUN npm run build --prefix ./flower/sepal


FROM node:23-alpine AS runner

WORKDIR /app

COPY --from=builder /app/flower/sepal /app/flower/sepal

ENV FLOWER_PROFILE=${FLOWER_PROFILE}
ENV NODE_ENV=production

EXPOSE 3000

CMD ["npm", "run", "start", "--prefix", "/app/flower/sepal"]