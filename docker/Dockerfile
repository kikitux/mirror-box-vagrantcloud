FROM openjdk:8-jre-alpine
COPY *.jar /root/
COPY run.sh /root/run.sh
RUN chmod +x /root/run.sh
WORKDIR /root
CMD ["./run.sh"]
