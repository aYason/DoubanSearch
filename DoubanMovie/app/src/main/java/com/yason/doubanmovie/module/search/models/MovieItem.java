package com.yason.doubanmovie.module.search.models;

/**
 * @author Yason
 * @since 2018/10/8
 */

public class MovieItem {

  private String md5UrlId;
  private String serialNumber;
  private String name;
  private String frontImageUrl;
  private String frontImagePath;
  private String director;
  private String actor;
  private String type;
  private String releaseDate;
  private String runtime;
  private String description;
  private String star;
  private String evaluation;

  public String getMd5UrlId() {
    return md5UrlId;
  }

  public void setMd5UrlId(String md5UrlId) {
    this.md5UrlId = md5UrlId;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFrontImageUrl() {
    return frontImageUrl;
  }

  public void setFrontImageUrl(String frontImageUrl) {
    this.frontImageUrl = frontImageUrl;
  }

  public String getFrontImagePath() {
    return frontImagePath;
  }

  public void setFrontImagePath(String frontImagePath) {
    this.frontImagePath = frontImagePath;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getActor() {
    return actor;
  }

  public void setActor(String actor) {
    this.actor = actor;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getRuntime() {
    return runtime;
  }

  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStar() {
    return star;
  }

  public void setStar(String star) {
    this.star = star;
  }

  public String getEvaluation() {
    return evaluation;
  }

  public void setEvaluation(String evaluation) {
    this.evaluation = evaluation;
  }

  @Override
  public String toString() {
    return "MovieItem{" +
        "md5UrlId='" + md5UrlId + '\'' +
        ", serialNumber='" + serialNumber + '\'' +
        ", name='" + name + '\'' +
        ", frontImageUrl='" + frontImageUrl + '\'' +
        ", frontImagePath='" + frontImagePath + '\'' +
        ", director='" + director + '\'' +
        ", actor='" + actor + '\'' +
        ", type='" + type + '\'' +
        ", releaseDate='" + releaseDate + '\'' +
        ", runtime='" + runtime + '\'' +
        ", description='" + description + '\'' +
        ", star='" + star + '\'' +
        ", evaluation='" + evaluation + '\'' +
        '}';
  }
}
