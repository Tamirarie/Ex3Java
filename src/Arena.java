import java.util.Arrays;
/*Setting surface-All parts of the arena
 * 
 */
public class Arena {		
	int ArenaSize;
	int Arena [][];
	int BlackPanel = -3;
	int GrayPanel = -2;
	int WhitePanel = -1;
	
	public Arena(int ArenaSize) {								//builder
		this.ArenaSize = ArenaSize;
		Arena = new int [ArenaSize][ArenaSize];
		
		for (int i = 0; i < ArenaSize; i++) {					//setting Panels to the arena
			Arrays.fill(Arena[i], GrayPanel);					
		}
		int i1 = 2,j1=2;
		int i2 = 4 , j2=4;
		fillArenaBlack(i1,j1,i2,j2);
		
		for (int i = 0; i < ArenaSize; i++) {					//Black Panel
			Arena[i][0] = BlackPanel;
			Arena[0][i] = BlackPanel;
			Arena[ArenaSize-1][i] = BlackPanel;
			Arena[i][ArenaSize-1] = BlackPanel;
		}
		for (int i = 1; i < ArenaSize-1; i++) {					//Gray Panel
			int rand = (int)(Math.random()*(ArenaSize-2) + 1);
			Arena[i][rand] = WhitePanel;
			
		}
	}
	
	private void fillArenaBlack(int i1, int j1, int i2, int j2) {
		for (int i = i1; i <= i2; i++) {
			for (int j = j1; j <= j2; j++) {
				Arena[i][j] = BlackPanel;
			}
		}
		
	}

	public static void main(String[] args) {
		Arena a = new Arena(10);
		for (int i = 0; i < a.ArenaSize; i++) {
			System.out.println(Arrays.toString(a.Arena[i]));
		}
	}
}
