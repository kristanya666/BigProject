FROM node:erbium-alpine3.12
WORKDIR opt/app
RUN npm install
COPY . .
EXPOSE 9999
CMD ["npm", "start"]

