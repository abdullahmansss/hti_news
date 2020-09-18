package softagi.ss.news.models;

import java.util.List;

public class NewsModel
{
    private String status;
    private List<NewsDetails> articles;

    public NewsModel(String status, List<NewsDetails> articles) {
        this.status = status;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewsDetails> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsDetails> articles) {
        this.articles = articles;
    }

    public class NewsDetails
    {
        private String title;
        private String urlToImage;
        private String publishedAt;

        public NewsDetails(String title, String urlToImage, String publishedAt) {
            this.title = title;
            this.urlToImage = urlToImage;
            this.publishedAt = publishedAt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }
    }
}