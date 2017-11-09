# A basic ubuntu docker file for transforming taf server at DigitalOcean into plain docker;)
FROM ubuntu

MAINTAINER Magnus Olsson (magnus.olsson@claremont.se)

RUN apt-get update

RUN apt-get install -y nginx
ENTRYPOINT [“/usr/sbin/nginx”,”-g”,”daemon off;”]

EXPOSE 80

#CMD ["/usr/sbin/apache2", "-D", "FOREGROUND"]
CMD ["bash"]