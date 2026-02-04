FROM node:18-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install --production

# Copy application files
COPY . .

# Create data directory
RUN mkdir -p /data

# Copy initial database to data directory
COPY db.json /data/

# Expose port
EXPOSE 3001

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://127.0.0.1:3001/reviews || exit 1

# Start the server
CMD ["npx", "json-server", "db.json", "--host", "0.0.0.0", "--port", "3001"]