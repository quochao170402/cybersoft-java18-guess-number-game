package cybersoft.javabackend.java18.game;

/**
 * GameSession đoán số.
 * Chương trình sinh ra một số tự nhiên từ 1 đến 1000
 * Mỗi lượt, người chơi nhập 1 con số để đoán.
 * Nếu chính xác, người dùng chiến thằng, lưu lại kết quả để xếp hạng.
 * Nếu không chính xác, hiện gợi ý cho người dùng.
 * 
 * Người dùng có thể chơi nhiều lần, mỗi lần gọi là một màn chơi.
 * Kết quản màn chơi được hiển thị trên mà hình home khi người chơi đăng nhập thành công
 * 
 * Màn hình màn chơi gồm ô nhập liệu cho phép người chơi nhập số mình đoán.
 * Các kết quả của các lần đoán trước được liệt kê theo dạng bảng để người chơi dễ theo dõi
 * và đưa ra lựa chọn chính xác cho lần đoán tiếp theo.
 * 
 * Màn hình xếp hạng liệt kê các màn chơi xuất sắc nhất, hiển thị id màn chơi,
 * thông tin người chơi, số lần đoán chính xác và thời gian màn chơi.
 *
 * @author Quoc Hao
 * @version@1.0
 * 
 */
public class Game {
	public void start() {
		System.out.println("GameSession started");
	}
}
