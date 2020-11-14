package bean;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
    private int id;
    private String author;
    private String title;
    private int count;
    private boolean readingRoom;
    private int usersId;

    public Book() {
        id = -1;
        author = "Noname";
        title = "Noname";
        count = -1;
        readingRoom = false;
        usersId = -1;
    }

    public Book(int id, String author, String title, int count, boolean readingRoom, int usersId) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.count = count;
        this.readingRoom = readingRoom;
        this.usersId = usersId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isReadingRoom() {
        return readingRoom;
    }

    public void setReadingRoom(boolean readingRoom) {
        this.readingRoom = readingRoom;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id == book.id &&
                count == book.count &&
                readingRoom == book.readingRoom &&
                usersId == book.usersId &&
                author.equals(book.author) &&
                title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, title, count, readingRoom, usersId);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", readingRoom=" + readingRoom +
                ", usersId=" + usersId +
                '}';
    }
}
