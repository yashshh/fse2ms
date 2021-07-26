From openjdk:8
copy ./target/auth.jar auth.jar
CMD ["java","-jar","auth.jar"]