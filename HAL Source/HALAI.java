import connectK.CKPlayer;
import connectK.BoardModel;
import java.awt.Point;

/**
This is a Connect-K AI program designed to play Connect-K intelligently 
with gravity on or gravity off using Iterative Deepening Search, Alpha-Beta Pruning, 
and a heuristic function to evaluate game states.

@author David Galanter
@version 12/11/15

*/

public class HALAI extends CKPlayer {
	
	private final int WIN_VAL = 1000;
	private final int LOSE_VAL = -1000;
	private final int POSITIVE_INFINITY = 100000;
	private final int NEGATIVE_INFINITY = -100000;
	private final int CANCEL_VAL = -111111;
	
	/**
	 * Constructor
	 * @param player byte that determines player 1 or 2
	 * @param state current state of the game
	 */
	public HALAI(byte player, BoardModel state) {
		super(player, state);
		teamName = "HAL";
	}
	
	/**
	 * Subclass of HAL used to store the heuristic value of a move 
	 * and the move itself.
	 * 
	 * @author David Galanter
	 *
	 */
	public class Action {
		private int value;
		private Point move;
		
		/**
		 * Constructor for Action class
		 * @param initVal heuristic value you want to store
		 * @param initPoint move that will result in the heuristic value
		 */
		public Action(int initVal, Point initPoint)
		{
			value = initVal;
			move = initPoint;
		}
		
		/**
		 * @return Action move
		 */
		public Point getPoint()
		{
			return move;
		}
		
		/**
		 * @return Action value
		 */
		public int getVal()
		{
			return value;
		}
		
		/**
		 * Change point to a different point
		 * @param newPoint new point you want to set
		 */
		public void setPoint(Point newPoint)
		{
			move = newPoint;
		}
		
		/**
		 * Change value to a different value
		 * @param newVal new value you want to set
		 */
		public void setVal(int newVal)
		{
			value = newVal;
		}
	}
	
	/**
	 * Potential is deemed as a space's capability of being part of a winning row, column, 
	 * or diagonal. This function checks a space's potential given a game state and coordinates.
	 * @param state the game state
	 * @param x x coordinate for the space in question
	 * @param y y coordinate for the space in question
	 * @return true if the space has potential
	 */
	public boolean hasPotential(BoardModel state, int x, int y)
	{
		boolean result = false;
		int width = state.getWidth();
		int height = state.getHeight();
		int k = state.getkLength();
		int tempx = x;
		int tempy = y;
		
		//CHECKING AI POTENTIAL
		//check left and right
		boolean go = true;

		int j = 0;
		while (j < k)
		{
			if (tempx >= 0 && tempx < width && tempx - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
						go = false;
					++i;
					--tempx;
				}
				
				if (go)
					return true;
				
			}
			
			go = true;
			++j;
			tempx = x + j;
			tempy = y;
		}
		
		
		//check up and down
		tempx = x;
		tempy = y;
		
		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
						go = false;
					++i;
					--tempy;
				}
				
				if (go)
					return true;
				
			}
			
			go = true;
			++j;
			tempx = x;
			tempy = y + j;
		}
		
		//check up left to down right diag
		tempx = x;
		tempy = y;
		
		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0
					&& tempx >= 0 && tempx < width && width - (tempx + 1) >= k)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
						go = false;
					++i;
					--tempy;
					++tempx;
				}
				
				if (go)
					return true;
				
			}
			
			go = true;
			++j;
			tempx = x - j;
			tempy = y + j;
		}
		
		
		//check up right diag to down left
		tempx = x;
		tempy = y;
		
		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0
					&& tempx >= 0 && tempx < width && tempx - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
						go = false;
					++i;
					--tempy;
					--tempx;
				}
				
				if (go)
					return true;
				
			}
			
			go = true;
			++j;
			tempx = x + j;
			tempy = y + j;
		}
		
		
		
		//CHECK OPPONENT POTENTIAL
		//check left and right
		tempx = x;
		tempy = y;
		go = true;

		j = 0;
		while (j < k)
		{
			if (tempx >= 0 && tempx < width && tempx - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == player)
						go = false;
					++i;
					--tempx;
				}

				if (go)
					return true;

			}

			go = true;
			++j;
			tempx = x + j;
			tempy = y;
		}


		//check up and down
		tempx = x;
		tempy = y;

		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == player)
						go = false;
					++i;
					--tempy;
				}

				if (go)
					return true;

			}

			go = true;
			++j;
			tempx = x;
			tempy = y + j;
		}

		//check up left to down right diag
		tempx = x;
		tempy = y;

		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0
					&& tempx >= 0 && tempx < width && width - (tempx + 1) >= k)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == player)
						go = false;
					++i;
					--tempy;
					++tempx;
				}

				if (go)
					return true;

			}

			go = true;
			++j;
			tempx = x - j;
			tempy = y + j;
		}


		//check up right diag to down left
		tempx = x;
		tempy = y;

		go = true;

		j = 0;
		while (j < k)
		{
			if (tempy >= 0 && tempy < height && tempy - (k - 1) >= 0
					&& tempx >= 0 && tempx < width && tempx - (k - 1) >= 0)
			{
				int i = 0;
				while (i < k && go)
				{
					if (state.getSpace(tempx, tempy) == player)
						go = false;
					++i;
					--tempy;
					--tempx;
				}

				if (go)
					return true;

			}

			go = true;
			++j;
			tempx = x + j;
			tempy = y + j;
		}
		
		return result;
	}
	
	/**
	 * A junction is a space that can be involved in two or more winning rows, columns, 
	 * or diagonals. This function checks if a space is a junction for the opponent.
	 * @param state state of the game to be considered
	 * @param x x coordinate for the space in question
	 * @param y y coordinate for the space in question
	 * @return true if the space is a junction for the opponent
	 */
	public boolean isTheirJunction(BoardModel state, int x, int y)
	{
		boolean result = false;
		
		int height = state.getHeight();
		int width = state.getWidth();
		int k = state.getkLength();
		int potentials = 0;
		int tempx = x;
		int tempy = y;
		
		//check left
		if (x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check right
		tempx = x;
		tempy = y;
		
		if (width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				++tempy;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down
		tempx = x;
		tempy = y;
		
		if (y >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				--tempy;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up left diag
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k && x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				++tempy;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up right diag
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k && width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				++tempy;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down right
		tempx = x;
		tempy = y;
		
		if (y >= k - 1 && width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				--tempy;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down left
		tempx = x;
		tempy = y;
		
		if (y >= k - 1 && x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == player)
					go = false;
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					++count;
				++i;
				--tempy;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		if (potentials >= 2)
			result = true;
		
		
		return result;
	}
	
	/**
	 * Compliment of isTheirJunction function. Checks if a space is a junction for the AI.
	 * @param state state of the game in question
	 * @param x x coordinate of the space in question
	 * @param y y coordinate of the space in question
	 * @return true if the space is a junction for AI
	 */
	public boolean isJunction(BoardModel state, int x, int y)
	{
		boolean result = false;
		
		int height = state.getHeight();
		int width = state.getWidth();
				
		int k = state.getkLength();
		int potentials = 0;
		int tempx = x;
		int tempy = y;
		
		//check left
		if (x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check right
		tempx = x;
		tempy = y;
		
		if (width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				++tempy;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down
		tempx = x;
		tempy = y;
		
		if (y >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				--tempy;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up left diag
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k && x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				++tempy;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check up right diag
		tempx = x;
		tempy = y;
		
		if (height - (y + 1) >= k && width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			++tempy;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				++tempy;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down right
		tempx = x;
		tempy = y;
		
		if (y >= k - 1 && width - (x + 1) >= k)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			++tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				--tempy;
				++tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		//check down left
		tempx = x;
		tempy = y;
		
		if (y >= k - 1 && x >= k - 1)
		{
			boolean go = true;
			int i = 0;
			int count = 0;
			--tempy;
			--tempx;
			while (i < k - 1 && go)
			{
				if (state.getSpace(tempx, tempy) == (byte)(player == 1 ? 2 : 1))
					go = false;
				if (state.getSpace(tempx, tempy) == player)
					++count;
				++i;
				--tempy;
				--tempx;
			}
			
			if (go && count == k - 2)
				++potentials;
		}
		
		if (potentials >= 2)
			result = true;
		
		
		return result;
	}
	
	/**
	 * Generates an array of possible moves that could be made next.
	 * @param gravity if the gravity for this game is on or off
	 * @param state state of the game in question
	 * @param maxCalled true if max called this function (the AI)
	 * @return an array of possible moves to consider
	 */
	public Point[] getPoints(boolean gravity, BoardModel state, boolean maxCalled)
	{
		if (gravity)
		 {
			Point[] myPoints = new Point[state.getWidth()];
			int width = state.getWidth();
			int height = state.getHeight();
			int mid = width / 2;
			int limit = state.getWidth() % 2 == 0 ? mid + 1 : mid;
			int count = 1;
			
			myPoints[0] = new Point(mid, height - 1);
			for (int i = 1; i <= limit; ++i)
			{
				if (!(mid - i < 0))
				{
					myPoints[count] = new Point(mid - i, height - 1);
					++count;
				}
				if (!(mid + i >= width))
				{
					myPoints[count] = new Point(mid + i, height - 1);
					++count;
				}
			}
			return myPoints;
		 }
		 else
		 {
			 /*
			  * Grabs potential points and disregards points that have no potential
			  */
			 int width = state.getWidth();
			 int height = state.getHeight();
			 int inc = 0;
			 int kLength = state.getkLength();
			 
			 
			 for (int i = 0; i < width; ++i)
			 {
				 for (int j = 0; j < height; ++j)
				 {
					 boolean add = false;
					 if (state.getSpace(i, j) == 0 && hasPotential(state, i, j))
					 {
						 int tempx = i - 1;
						 int tempy = j + 1;
						 
						 for (int k = 0; k < 3; ++k)
						 {
							 for (int l = 0; l < 3; ++l)
							 {
								 if ((tempx >= 0 && tempx < width
										 && tempy >= 0 && tempy < height
										 && !(tempy == j && tempx == i)
										 && state.getSpace(tempx, tempy) != 0))
								 {
									add = true;
								 }
								 ++tempx;
							 }
							 --tempy;
							 tempx = i - 1;
						 }
						 if (add || (kLength >= 3 && isJunction(state, i, j))
								 || (kLength >= 3 && isTheirJunction(state, i, j)))
							 ++inc;
						 
					 }
					 
				 }
			 }
			 
			 Point[] myPoints = new Point[inc];
			 double[] euclids = new double[inc];
			 Point[] finalPoints = new Point[inc];
			 int size = inc;
			 inc = 0;
			 
			 for (int i = 0; i < width; ++i)
			 {
				 for (int j = 0; j < height; ++j)
				 {
					 boolean add = false;
					 if (state.getSpace(i, j) == 0 && hasPotential(state, i, j))
					 {
						 int tempx = i - 1;
						 int tempy = j + 1;
						 
						 for (int k = 0; k < 3; ++k)
						 {
							 for (int l = 0; l < 3; ++l)
							 {
								 if ((tempx >= 0 && tempx < width
										 && tempy >= 0 && tempy < height
										 && !(tempy == j && tempx == i)
										 && state.getSpace(tempx, tempy) != 0))
								 { 
									add = true;
								 }
								 ++tempx;
							 }
							 --tempy;
							 tempx = i - 1;
						 }
						 
						 if (add || (kLength >= 3 && isJunction(state, i, j))
								 || (kLength >= 3 && isTheirJunction(state, i, j)))
						 {
							 myPoints[inc] = new Point(i, j);
							 ++inc;
						 }
						 
					 }
					 
				 }
			 }
			 
			 /*
			  * If disregarding points results in an empty array, return an array with points 
			  * that possibly have no potential (most likely towards the end of the game).
			  */
			 if (size <= 0)
			 {
				 inc = 0;
				 for (int i = 0; i < width; ++i)
				 {
					 for (int j = 0; j < height; ++j)
					 {
						 boolean add = false;
						 if (state.getSpace(i, j) == 0)
						 {
							 int tempx = i - 1;
							 int tempy = j + 1;
							 
							 for (int k = 0; k < 3; ++k)
							 {
								 for (int l = 0; l < 3; ++l)
								 {
									 if ((tempx >= 0 && tempx < width
											 && tempy >= 0 && tempy < height
											 && !(tempy == j && tempx == i)
											 && state.getSpace(tempx, tempy) != 0))
									 {
										add = true;
									 }
									 ++tempx;
								 }
								 --tempy;
								 tempx = i - 1;
							 }
							 if (add || (kLength >= 3 && isJunction(state, i, j))
									 || (kLength >= 3 && isTheirJunction(state, i, j)))
								 ++inc;
							 
						 }
						 
					 }
				 }
				 
				 myPoints = new Point[inc];
				 euclids = new double[inc];
				 finalPoints = new Point[inc];
				 size = inc;
				 inc = 0;
				 
				 for (int i = 0; i < width; ++i)
				 {
					 for (int j = 0; j < height; ++j)
					 {
						 boolean add = false;
						 if (state.getSpace(i, j) == 0)
						 {
							 int tempx = i - 1;
							 int tempy = j + 1;
							 
							 for (int k = 0; k < 3; ++k)
							 {
								 for (int l = 0; l < 3; ++l)
								 {
									 if ((tempx >= 0 && tempx < width
											 && tempy >= 0 && tempy < height
											 && !(tempy == j && tempx == i)
											 && state.getSpace(tempx, tempy) != 0))
									 { 
										add = true;
									 }
									 ++tempx;
								 }
								 --tempy;
								 tempx = i - 1;
							 }
							 
							 if (add || (kLength >= 3 && isJunction(state, i, j))
									 || (kLength >= 3 && isTheirJunction(state, i, j)))
							 {
								 myPoints[inc] = new Point(i, j);
								 ++inc;
							 }
							 
						 }
						 
					 }
				 }
			 }
			 
			 /*
			  * This figures out the euclidean distance between all points being considered 
			  * and the center of the board. The AI will always favor spaces that are closest 
			  * to the center (shortest euclidean distances).
			  */
			 //initialize euclids
			 Point middle = new Point(state.getWidth() / 2, state.getHeight() / 2);
			 
			 for (int i = 0; i < size; ++i)
			 {
				 euclids[i] = Math.sqrt(Math.pow((myPoints[i].x - middle.x), 2) 
						 + Math.pow((myPoints[i].y - middle.y), 2));
			 }
			 
			 //initialize finalPoints
			 int k = 0;
			 int solidSize = size;
			 while (k < solidSize)
			 {
				 int smallest = 0;
				 for (int i = 1; i < size; ++i)
				 {
					 if (euclids[i] < euclids[smallest])
						 smallest = i;
				 }
				 finalPoints[k] = myPoints[smallest];
				 
				 for (int i = smallest; i < size - 1; ++i)
				 {
					 myPoints[i] = myPoints[i + 1];
					 euclids[i] = euclids[i + 1];
				 }
				 
				 ++k;
				 --size;
			 }
			 
			 
			 
			 return finalPoints;
		 }
	}
	
	/**
	 * This is the AI's heuristic function. This is how it evaluates if it is doing well 
	 * or not. The higher the number returned, the better for the AI. The lower, the worse. 
	 * This function considers quite a few different strategies in order to return a number that is 
	 * as accurate as possible.
	 * @param state state of the game that is being considered
	 * @return a number representing how good or bad this state is for it.
	 */
	public int hFunc(BoardModel state)
	{
		if (state.gravity)
		{
			int kLength = state.getkLength();
			int width = state.getWidth();
			int height = state.getHeight();
			int myTotal = 0;
			int theirTotal = 0;
			
			//groups of different length rows, columns, or diagonals
			int[] myGroups = new int[kLength + 1];
			int myGroupsLength = myGroups.length;
			int[] theirGroups = new int[kLength + 1];
			int theirGroupsLength = theirGroups.length;
			
			//Variables for threat spaces
			int domain = state.getHeight() * state.getWidth();
			Point[] myThreats = new Point[domain];
			int myThreatsSize = 0;
			int theirThreatsSize = 0;
			Point[] theirThreats = new Point[domain];
			
			//Even odd strategy variables
			int myUnsharedOddThreats = 0;
			int theirUnsharedOddThreats = 0;
			int myUnsharedEvenThreats = 0;
			int theirUnsharedEvenThreats = 0;
			int mySharedOddThreats = 0;
			int mySharedEvenThreats = 0;
			int theirSharedOddThreats = 0;
			int theirSharedEvenThreats = 0;

			/*
			 * First we check how many different consecutive squares the AI and the opponent 
			 * has in a row, column, or diagonal and what length they are.
			 */
			//check MY horizontal
			Point lastEmpty = new Point(0, 0);
			for (int j = 0; j < height; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(k, j) == player)
							++num;
						else if (state.getSpace(k, j) == (byte)(player == 1? 2 : 1))
							go = false;
						else
							lastEmpty = new Point(k, j);
						++k;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int l = 0; l < myThreatsSize; ++l)
						{
							if (lastEmpty.equals(myThreats[l]))
								add = false;
						}
						if (add)
						{
							myThreats[myThreatsSize] = lastEmpty;
							++myThreatsSize;
						}
					}
					
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their horizontal
			for (int j = 0; j < height; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(k, j) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(k, j) == player)
							go = false;
						else
							lastEmpty = new Point(k, j);
						++k;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int l = 0; l < theirThreatsSize; ++l)
						{
							if (lastEmpty.equals(theirThreats[l]))
								add = false;
						}
						if (add)
						{
							theirThreats[theirThreatsSize] = lastEmpty;
							++theirThreatsSize;
						}
					}
					
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my Vertical
			for (int j = 0; j < width; ++j)
			{
				for (int i = 0; i <= height - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(j, k) == player)
							++num;
						else if (state.getSpace(j, k) == (byte)(player == 1? 2 : 1))
							go = false;
						else
							lastEmpty = new Point(j, k);
						++k;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int l = 0; l < myThreatsSize; ++l)
						{
							if (lastEmpty.equals(myThreats[l]))
								add = false;
						}
						if (add)
						{
							myThreats[myThreatsSize] = lastEmpty;
							++myThreatsSize;
						}
					}
					
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their vertical
			for (int j = 0; j < width; ++j)
			{
				for (int i = 0; i <= height - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(j, k) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(j, k) == player)
							go = false;
						else
							lastEmpty = new Point(j, k);
						++k;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int l = 0; l < theirThreatsSize; ++l)
						{
							if (lastEmpty.equals(theirThreats[l]))
								add = false;
						}
						if (add)
						{
							theirThreats[theirThreatsSize] = lastEmpty;
							++theirThreatsSize;
						}
					}
					
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my bottom left to top right diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth < i + kLength && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == player)
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							go = false;
						else
							lastEmpty = new Point(tempWidth, tempHeight);
						++tempHeight;
						++tempWidth;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int k = 0; k < myThreatsSize; ++k)
						{
							if (lastEmpty.equals(myThreats[k]))
								add = false;
						}
						if (add)
						{
							myThreats[myThreatsSize] = lastEmpty;
							++myThreatsSize;
						}
					}
					
					if (go && num != 0)
						++myGroups[num];
				}
			}


			//Check their bottom left to top right diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth < i + kLength && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == player)
							go = false;
						else
							lastEmpty = new Point(tempWidth, tempHeight);
						++tempHeight;
						++tempWidth;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int k = 0; k < theirThreatsSize; ++k)
						{
							if (lastEmpty.equals(theirThreats[k]))
								add = false;
						}
						if (add)
						{
							theirThreats[theirThreatsSize] = lastEmpty;
							++theirThreatsSize;
						}
					}
					
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my bottom right to top left diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = kLength - 1; i < width; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth >= i - (kLength - 1) && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == player)
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							go = false;
						else
							lastEmpty = new Point(tempWidth, tempHeight);
						++tempHeight;
						--tempWidth;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int k = 0; k < myThreatsSize; ++k)
						{
							if (lastEmpty.equals(myThreats[k]))
								add = false;
						}
						if (add)
						{
							myThreats[myThreatsSize] = lastEmpty;
							++myThreatsSize;
						}
					}
					
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their bottom right to top left diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = kLength - 1; i < width; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth >= i - (kLength - 1) && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == player)
							go = false;
						else
							lastEmpty = new Point(tempWidth, tempHeight);
						++tempHeight;
						--tempWidth;
					}
					
					if (num == kLength - 1 && lastEmpty.y - 1 >= 0 
							&& state.getSpace(lastEmpty.x, lastEmpty.y - 1) == 0 && go)
					{
						boolean add = true;
						for (int k = 0; k < theirThreatsSize; ++k)
						{
							if (lastEmpty.equals(theirThreats[k]))
								add = false;
						}
						if (add)
						{
							theirThreats[theirThreatsSize] = lastEmpty;
							++theirThreatsSize;
						}
					}
					
					if (go && num != 0)
						++theirGroups[num];
				}
			}
			
			/*
			 * Any empty square that sits atop another empty square and is also the final 
			 * square needed for a win is called a threat. This next part figures out how many threats 
			 * each player has. It also figures out if the threat is on an even or odd row and if 
			 * the threat is shared or not (essential for certain strategies).
			 */
			//Get my threats
			//4 arrays
			boolean[] myHasUnsharedOdd = new boolean[width];
			boolean[] myHasUnsharedEven = new boolean[width];
			boolean[] myHasSharedOdd = new boolean[width];
			boolean[] myHasSharedEven = new boolean[width];
			for (int i = 0; i < myThreatsSize; ++i)
			{
				boolean shared = false;
				
				for (int j = 0; j < theirThreatsSize; ++j)
				{
					if (myThreats[i].x == theirThreats[j].x && myThreats[i].y >= theirThreats[j].y)
						shared = true;
				}
				
				if (shared)
				{
					if ((myThreats[i].y + 1) % 2 == 1 && !myHasSharedOdd[myThreats[i].x])
					{
						++mySharedOddThreats;
						myHasSharedOdd[myThreats[i].x] = true;
					}
					else if ((myThreats[i].y + 1) % 2 == 0 && !myHasSharedEven[myThreats[i].x])
					{
						++mySharedEvenThreats;
						myHasSharedEven[myThreats[i].x] = true;
					}
				}
				else
				{
					if ((myThreats[i].y + 1) % 2 == 1 && !myHasUnsharedOdd[myThreats[i].x])
					{
						++myUnsharedOddThreats;
						myHasUnsharedOdd[myThreats[i].x] = true;
					}
					else if ((myThreats[i].y + 1) % 2 == 0 && !myHasUnsharedEven[myThreats[i].x])
					{
						++myUnsharedEvenThreats;
						myHasUnsharedEven[myThreats[i].x] = true;
					}
				}
			}
			
			//get their threats
			//4 arrays
			boolean[] themHasUnsharedOdd = new boolean[width];
			boolean[] themHasUnsharedEven = new boolean[width];
			boolean[] themHasSharedOdd = new boolean[width];
			boolean[] themHasSharedEven = new boolean[width];
			for (int i = 0; i < theirThreatsSize; ++i)
			{
				boolean shared = false;
				
				for (int j = 0; j < myThreatsSize; ++j)
				{
					if (theirThreats[i].x == myThreats[j].x && theirThreats[i].y >= myThreats[j].y)
						shared = true;
				}
				
				if (shared)
				{
					if ((theirThreats[i].y + 1) % 2 == 1 && !themHasSharedOdd[theirThreats[i].x])
					{
						++theirSharedOddThreats;
						themHasSharedOdd[theirThreats[i].x] = true;
					}
					else if ((theirThreats[i].y + 1) % 2 == 0 && !themHasSharedEven[theirThreats[i].x])
					{
						++theirSharedEvenThreats;
						themHasSharedEven[theirThreats[i].x] = true;
					}
				}
				else
				{
					if ((theirThreats[i].y + 1) % 2 ==1 && !themHasUnsharedOdd[theirThreats[i].x])
					{
						++theirUnsharedOddThreats;
						themHasUnsharedOdd[theirThreats[i].x] = true;
					}
					else if ((theirThreats[i].y + 1) % 2 == 0 && !themHasUnsharedEven[theirThreats[i].x])
					{
						++theirUnsharedEvenThreats;
						themHasUnsharedEven[theirThreats[i].x] = true;
					}
				}
			}
			
			
			/*
			 * This is for the even odd strategy. Having certain threats on certain rows will result 
			 * in a value bonus or penalty.
			 */
			//AI is first player
			if (player == 1)
			{
				if (height % 2 == 0)
				{
					//What I need to win
					if (((myUnsharedOddThreats - 1) == theirUnsharedOddThreats) 
							|| ((myUnsharedOddThreats == theirUnsharedOddThreats) && (mySharedOddThreats % 2 == 1))
							|| (theirUnsharedOddThreats == 0 && (mySharedOddThreats + myUnsharedOddThreats) % 2 == 1))
						myTotal += 100;

					//What he needs to win
					if (((myUnsharedOddThreats + mySharedOddThreats) == 0 && (theirSharedEvenThreats + theirUnsharedEvenThreats) > 0)
							|| ((theirUnsharedOddThreats - 2) == myUnsharedOddThreats)
							|| ((myUnsharedOddThreats == theirUnsharedOddThreats) && (theirSharedOddThreats % 2 == 0 && theirSharedOddThreats > 0))
							|| ((theirUnsharedOddThreats - 1) == myUnsharedOddThreats && theirSharedOddThreats > 0)
							|| (myUnsharedOddThreats == 0 && (theirUnsharedOddThreats == 1 && theirSharedOddThreats > 0))
							|| (((theirUnsharedOddThreats + theirSharedOddThreats) % 2 == 0 && (theirUnsharedOddThreats + theirSharedOddThreats) > 0) && myUnsharedOddThreats == 0))
						theirTotal += 100;
				}
				else if (domain % 2 == 0 && height % 2 == 1)
				{
					//what i need to win
					if (((myUnsharedEvenThreats - 1) == theirUnsharedEvenThreats)
							|| (mySharedEvenThreats % 2 == 1)
							|| ((mySharedEvenThreats + myUnsharedEvenThreats) == 1 && (theirSharedOddThreats + theirUnsharedOddThreats) == 1))
						myTotal += 100;

					//what he needs to win
					if (((theirSharedOddThreats + theirUnsharedOddThreats) > 0)
							|| (((theirSharedEvenThreats + theirUnsharedEvenThreats) % 2 == 0 && (theirSharedEvenThreats + theirUnsharedEvenThreats) > 0)
							&& (((theirUnsharedEvenThreats - 2) == myUnsharedEvenThreats)
									|| (theirSharedEvenThreats == mySharedEvenThreats))))
						theirTotal += 100;

				}
				else if (domain % 2 == 1)
				{
					if (((mySharedOddThreats + myUnsharedOddThreats) > 0)
							|| (((mySharedEvenThreats + myUnsharedEvenThreats) % 2 == 0 && (mySharedEvenThreats + myUnsharedEvenThreats) > 0)
							&& ((myUnsharedEvenThreats - 2 == theirUnsharedEvenThreats)
									|| (mySharedEvenThreats == theirSharedEvenThreats))))
						myTotal += 100;
					
					if ((theirUnsharedEvenThreats - 1 == myUnsharedEvenThreats)
							|| (theirSharedEvenThreats % 2 == 1)
							|| ((theirSharedEvenThreats + theirUnsharedEvenThreats) == 1
							&& (mySharedOddThreats + myUnsharedOddThreats) == 1))
						theirTotal += 100;
				}
			}
			//AI is second player
			else
			{
				if (height % 2 == 0)
				{
					//What he needs to win
					if (((theirUnsharedOddThreats - 1) == myUnsharedOddThreats) 
							|| ((theirUnsharedOddThreats == myUnsharedOddThreats) && (theirSharedOddThreats % 2 == 1))
							|| (myUnsharedOddThreats == 0 && (theirSharedOddThreats + theirUnsharedOddThreats) % 2 == 1))
						theirTotal += 100;

					//What i need to win
					if (((theirUnsharedOddThreats + theirSharedOddThreats) == 0 && (mySharedEvenThreats + myUnsharedEvenThreats) > 0)
							|| ((myUnsharedOddThreats - 2) == theirUnsharedOddThreats)
							|| ((theirUnsharedOddThreats == myUnsharedOddThreats) && (mySharedOddThreats % 2 == 0 && mySharedOddThreats > 0))
							|| ((myUnsharedOddThreats - 1) == theirUnsharedOddThreats && mySharedOddThreats > 0)
							|| (theirUnsharedOddThreats == 0 && (myUnsharedOddThreats == 1 && mySharedOddThreats > 0))
							|| (((myUnsharedOddThreats + mySharedOddThreats) % 2 == 0 && (myUnsharedOddThreats + mySharedOddThreats) > 0) && theirUnsharedOddThreats == 0))
						myTotal += 100;
				}
				else if (domain % 2 == 0 && height % 2 == 1)
				{
					//what he needs to win
					if (((theirUnsharedEvenThreats - 1) == myUnsharedEvenThreats)
							|| (theirSharedEvenThreats % 2 == 1)
							|| ((theirSharedEvenThreats + theirUnsharedEvenThreats) == 1 && (mySharedOddThreats + myUnsharedOddThreats) == 1))
						theirTotal += 100;

					//what i need to win
					if (((mySharedOddThreats + myUnsharedOddThreats) > 0)
							|| (((mySharedEvenThreats + myUnsharedEvenThreats) % 2 == 0 && (mySharedEvenThreats + myUnsharedEvenThreats) > 0)
							&& (((myUnsharedEvenThreats - 2) == theirUnsharedEvenThreats)
									|| (mySharedEvenThreats == theirSharedEvenThreats))))
						myTotal += 100;
				}
				else if (domain % 2 == 1)
				{
					//what they need
					if (((theirSharedOddThreats + theirUnsharedOddThreats) > 0)
							|| (((theirSharedEvenThreats + theirUnsharedEvenThreats) % 2 == 0 && (theirSharedEvenThreats + theirUnsharedEvenThreats) > 0)
							&& ((theirUnsharedEvenThreats - 2 == myUnsharedEvenThreats)
									|| (theirSharedEvenThreats == mySharedEvenThreats))))
						theirTotal += 100;
					
					//what i need
					if ((myUnsharedEvenThreats - 1 == theirUnsharedEvenThreats)
							|| (mySharedEvenThreats % 2 == 1)
							|| ((mySharedEvenThreats + myUnsharedEvenThreats) == 1
							&& (theirSharedOddThreats + theirUnsharedOddThreats) == 1))
						myTotal += 100;
				}
			}
			
			/*
			 * This gets the total score for both players, scaling longer groupings 
			 * for more points.
			 */
			//get my total
			int scale = 1;
			for (int i = 2; i < myGroupsLength; ++i)
			{
				myTotal += (myGroups[i] * scale);
				++scale;
			}

			//get their total
			scale = 1;
			for (int i = 2; i < theirGroupsLength; ++i)
			{
				theirTotal += (theirGroups[i] * scale);
				++scale;
			}

			if (theirGroups[kLength] > 0)
				return LOSE_VAL;
			else if (myGroups[kLength] > 0)
				return WIN_VAL;
			else
			{
				if (myTotal - theirTotal == 0)
				{
					if (player == 2)
						return 1;
					else
						return -1;
				}
				
				return myTotal - theirTotal;
			}
		}
		else
		{
			//For different length groups for each player
			int kLength = state.getkLength();
			int[] myGroups = new int[kLength + 1];
			int myGroupsLength = myGroups.length;
			int[] theirGroups = new int[kLength + 1];
			int theirGroupsLength = theirGroups.length;
			
			
			int width = state.getWidth();
			int height = state.getHeight();
			int myTotal = 0;
			int theirTotal = 0;

			/*
			 * Like with gravity on, we check groupings of spaces in every direction 
			 * for each player.
			 */
			//check MY horizontal
			for (int j = 0; j < height; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(k, j) == player)
							++num;
						else if (state.getSpace(k, j) == (byte)(player == 1? 2 : 1))
							go = false;
						++k;
					}
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their horizontal
			for (int j = 0; j < height; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(k, j) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(k, j) == player)
							go = false;
						++k;
					}
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my Vertical
			for (int j = 0; j < width; ++j)
			{
				for (int i = 0; i <= height - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(j, k) == player)
							++num;
						else if (state.getSpace(j, k) == (byte)(player == 1? 2 : 1))
							go = false;
						++k;
					}
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their vertical
			for (int j = 0; j < width; ++j)
			{
				for (int i = 0; i <= height - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int k = i;
					while (k < i + kLength && go)
					{
						if (state.getSpace(j, k) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(j, k) == player)
							go = false;
						++k;
					}
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my bottom left to top right diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth < i + kLength && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == player)
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							go = false;
						++tempHeight;
						++tempWidth;
					}
					if (go && num != 0)
						++myGroups[num];
				}
			}


			//Check their bottom left to top right diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = 0; i <= width - kLength; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth < i + kLength && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == player)
							go = false;
						++tempHeight;
						++tempWidth;
					}
					if (go && num != 0)
						++theirGroups[num];
				}
			}

			//Check my bottom right to top left diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = kLength - 1; i < width; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth >= i - (kLength - 1) && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == player)
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							go = false;
						++tempHeight;
						--tempWidth;
					}
					if (go && num != 0)
						++myGroups[num];
				}
			}

			//Check their bottom right to top left diagonals
			for (int j = 0; j <= height - kLength; ++j)
			{
				for (int i = kLength - 1; i < width; ++i)
				{
					int num = 0;
					boolean go = true;
					int tempHeight = j;
					int tempWidth = i;

					while (tempHeight < j + kLength && tempWidth >= i - (kLength - 1) && go)
					{
						if (state.getSpace(tempWidth, tempHeight) == (byte)(player == 1? 2 : 1))
							++num;
						else if (state.getSpace(tempWidth, tempHeight) == player)
							go = false;
						++tempHeight;
						--tempWidth;
					}
					if (go && num != 0)
						++theirGroups[num];
				}
			}
			
			/*
			 * We now check for junctions (spaces that can be included in more 
			 * than one grouping).
			 */
			//Check junctions
			//do i have a junction
			boolean iHaveJunc = false;
			boolean iHaveJuncEmpty = false;
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j)
				{
					if (state.getSpace(i, j) == player)
						iHaveJunc = isJunction(state, i, j);
					else if (state.getSpace(i, j) == 0)
						iHaveJuncEmpty = isJunction(state, i, j);
				}
			
			if (iHaveJunc)
				myTotal += 100;
			else if (iHaveJuncEmpty)
				myTotal += 50;
			
			//do they have a junction
			boolean theyHaveJunc = false;
			boolean theyHaveJuncEmpty = false;
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j)
				{
					if (state.getSpace(i, j) == (byte)(player == 1? 2 : 1))
						theyHaveJunc = isTheirJunction(state, i, j);
					else if (state.getSpace(i, j) == 0)
						theyHaveJuncEmpty = isTheirJunction(state, i, j);
				}
			
			if (theyHaveJunc)
				theirTotal += 100;
			else if (theyHaveJuncEmpty)
				theirTotal += 50;

			//get Total
			//get my total
			int scale = 1;
			for (int i = 2; i < myGroupsLength; ++i)
			{
				myTotal += (myGroups[i] * scale);
				++scale;
			}

			//get their total
			scale = 1;
			for (int i = 2; i < theirGroupsLength; ++i)
			{
				theirTotal += (theirGroups[i] * scale);
				++scale;
			}

			if (theirGroups[kLength] > 0)
				return LOSE_VAL;
			else if (myGroups[kLength] > 0)
				return WIN_VAL;
			else
			{
				if (myTotal - theirTotal == 0)
				{
					if (player == 2)
						return 1;
					else
						return -1;
				}
				
				return myTotal - theirTotal;
			}
		}

	}
	
	/**
	 * Figures out what move should be made with the gravity off.
	 * @param state state of the game board to be considered
	 * @param deadline how much time in milliseconds is allowed to find a move
	 * @param start starting time when function was called
	 * @return move that should be made
	 */
	public Point gravOffMove(BoardModel state, int deadline, long start)
	{
		if (state.spacesLeft == (state.getHeight() * state.getWidth()))
			return new Point(state.getWidth() / 2, state.getHeight() / 2);
		else
			return alphaBetaDeepening(state, deadline, start);
	}
	
	/**
	 * Search tree with alpha beta pruning and iterative deepening.
	 * @param state state of the game to be considered
	 * @param deadline how much time in milliseconds is allowed to make a move.
	 * @param start time when execution of function started
	 * @return the move that should be made
	 */
	public Point alphaBetaDeepening(BoardModel state, int deadline, long start)
	{
		boolean go = true;
		Action best = new Action(0, new Point(0, 0));
		int i = 1;
		Action temp = new Action(0, new Point(0, 0));
		
		
		Action[] foundActs = new Action[state.getWidth() * state.getHeight()];
		int size = 0;
		
		while (go)
		{
			
			temp = firstMax(state, NEGATIVE_INFINITY, POSITIVE_INFINITY, i, deadline, start);
			
			foundActs[size] = temp;
			++size;
			
			if (temp.getVal() == WIN_VAL)
			{
				//Uncomment below for testing
//				System.out.println("Moves Ahead: " + size);
//				BoardModel tempBoard = state.placePiece(temp.getPoint(), player);
//				System.out.println("Current Val: " + hFunc(tempBoard));
//				System.out.println("Expected outcome: " + temp.getVal());
				
				return temp.getPoint();
			}
			
			if (temp.getVal() == CANCEL_VAL)
			{
				--size;
				
				//Uncomment below for testing
//				System.out.println("Moves Ahead: " + size);
				
				if (best.getVal() == LOSE_VAL)
				{
					boolean doit = true;
					
					while (size - 1 >= 0 && doit)
					{
						if (foundActs[size - 1].getVal() != LOSE_VAL)
						{
							best = foundActs[size - 1];
							doit = false;
						}
						--size;
					}
				}
				
				//Uncomment below for testing
//				BoardModel tempBoard = state.placePiece(best.getPoint(), player);
//				System.out.println("Current Val: " + hFunc(tempBoard));
//				System.out.println("Expected outcome: " + best.getVal());
				
				return best.getPoint();
			}
			
			if (temp.getVal() == LOSE_VAL)
			{
				//Uncomment below for testing
//				System.out.println("Moves Ahead: " + size);
				boolean doit = true;
				
				while (size - 1 >= 0 && doit)
				{
					if (foundActs[size - 1].getVal() != LOSE_VAL)
					{
						best = foundActs[size - 1];
						doit = false;
					}
					--size;
				}
				
				//Uncomment below for testing
//				BoardModel tempBoard = state.placePiece(temp.getPoint(), player);
//				System.out.println("Current Val: " + hFunc(tempBoard));
//				System.out.println("Expected outcome: " + temp.getVal());
				
				return best.getPoint();
			}
			
			if (temp.getVal() == 0)
			{
				//Uncomment below for testing
//				System.out.println("Moves Ahead: " + size);
//				BoardModel tempBoard = state.placePiece(temp.getPoint(), player);
//				System.out.println("Current Val: " + hFunc(tempBoard));
//				System.out.println("Expected outcome: " + temp.getVal());
				return temp.getPoint();
			}
			
			
			best = temp;
			++i;
			
			long myMillys = System.currentTimeMillis() - start + 50;
			if (myMillys >= deadline)
				go = false;
		}

		//Uncomment below for testing
//		System.out.println("Moves Ahead: " + size);
		
		if (best.getVal() == LOSE_VAL)
		{
			boolean doit = true;
			
			while (size - 1 >= 0 && doit)
			{
				if (foundActs[size - 1].getVal() != LOSE_VAL)
				{
					best = foundActs[size - 1];
					doit = false;
				}
				--size;
			}
		}
		
		//Uncomment below for testing
//		BoardModel tempBoard = state.placePiece(best.getPoint(), player);
//		System.out.println("Current Val: " + hFunc(tempBoard));
//		System.out.println("Expected outcome: " + best.getVal());
		
		return best.getPoint();
		
	}
	
	/**
	 * Alpha Beta pruning max function to be called on the first alpha beta function call.
	 * @param state current state of the board in question
	 * @param alpha value for alpha
	 * @param beta value for beta
	 * @param limit maximum search depth
	 * @param deadline how much time in milliseconds thats allowed
	 * @param start time the alphabeta started
	 * @return Action of the best move for max
	 */
	public Action firstMax(BoardModel state, int alpha, int beta, int limit, int deadline, long start)
	{
		
		int v = NEGATIVE_INFINITY;
		Point[] myPoints = getPoints(state.gravity, state, true);
		int size = myPoints.length;
		Action bestAct = new Action(0, new Point(0, 0));
		
		//Uncomment below for testing
//		System.out.println("The Points:");
//		for (int i = 0; i < size; ++i)
//		{
//			System.out.println("x: " + myPoints[i].x + " y: " + myPoints[i].y);
//		}


		for (int i = 0; i < size; ++i)
		{
			if (state.getSpace(myPoints[i]) == 0)
			{
				BoardModel temp = state.placePiece(myPoints[i], player);
				byte win = temp.winner();
				
				if (win == player)
					return new Action(WIN_VAL, myPoints[i]);
				else if (win == (byte)(player == 1 ? 2 : 1))
					v = Math.max(v, LOSE_VAL);
				else if (win == 0)
					v = Math.max(v, 0);
				else
					v = Math.max(v, minVal(temp, alpha, beta, limit - 1, deadline, start));
				
				if (v == WIN_VAL)
				{
					return new Action(v, myPoints[i]);
				}
				
				long myMillys = System.currentTimeMillis() - start + 50;
				if (myMillys >= deadline)
				{
					return new Action(CANCEL_VAL, new Point(0, 0));
				}
				
				if (v > alpha)
				{
					bestAct = new Action(v, myPoints[i]);
				}
				alpha = Math.max(alpha, v);
			}
		}
		
		return bestAct;
	}
	
	/**
	 * Alpha beta pruning max function.
	 * @param state state of the game board being considered
	 * @param alpha value of alpha
	 * @param beta value of beta
	 * @param limit maximum search depth
	 * @param deadline maximum time in milliseconds to search
	 * @param start start time of alphabeta function
	 * @return best move for max
	 */
	public int maxVal(BoardModel state, int alpha, int beta, int limit, int deadline, long start)
	{
		if (limit <= 0)
			return hFunc(state);
		
		int v = NEGATIVE_INFINITY;
		Point[] myPoints = getPoints(state.gravity, state, true);
		int size = myPoints.length;
		
		for (int i = 0; i < size; ++i)
		{
			if (state.getSpace(myPoints[i]) == 0)
			{
				BoardModel temp = state.placePiece(myPoints[i], player);
				byte win = temp.winner();
				
				long myMillys = System.currentTimeMillis() - start + 50;
				if (myMillys >= deadline)
					return CANCEL_VAL;
				
				if (win == player)
					return WIN_VAL;
				else if (win == (byte)(player == 1 ? 2 : 1))
					v = Math.max(v, LOSE_VAL);
				else if (win == 0)
					v = Math.max(v, 0);
				else
					v = Math.max(v, minVal(temp, alpha, beta, limit - 1, deadline, start));
				
				if (v >= beta)
					return v;
				alpha = Math.max(alpha, v);
			}
		}
		
		return v;
	}
	
	/**
	 * Min function for alpha beta pruning.
	 * @param state state of game board being considered.
	 * @param alpha value of alpha
	 * @param beta value of beta
	 * @param limit maximum search depth
	 * @param deadline maximum time in milliseconds allowed
	 * @param start time alphabeta started
	 * @return best move for min
	 */
	public int minVal(BoardModel state, int alpha, int beta, int limit, int deadline, long start)
	{
		if (limit <= 0)
			return hFunc(state);
		
		int v = POSITIVE_INFINITY;
		Point[] myPoints = getPoints(state.gravity, state, false);
		int size = myPoints.length;
		
		for (int i = 0; i < size; ++i)
		{
			if (state.getSpace(myPoints[i]) == 0)
			{
				BoardModel temp = state.placePiece(myPoints[i], (byte)(player == 1? 2 : 1));
				byte win = temp.winner();
				
				long myMillys = System.currentTimeMillis() - start + 50;
				if (myMillys >= deadline)
					return CANCEL_VAL;
				
				if (win == (byte)(player == 1 ? 2 : 1))
					return LOSE_VAL;
				else if (win == player)
					v = Math.min(v, WIN_VAL);
				else if (win == 0)
					v = Math.min(v, 0);
				else
					v = Math.min(v, maxVal(temp, alpha, beta, limit - 1, deadline, start));
				
				if (v <= alpha)
					return v;
				beta = Math.min(beta, v);
			}
		}
		
		return v;
	}
	
	/**
	 * Dummy function that returns unintelligent moves
	 * @param state state of the game board
	 * @return move to be made
	 */
	@Override
	public Point getMove(BoardModel state) {
		for(int i=3; i<state.getWidth(); ++i)
			for(int j=3; j<state.getHeight(); ++j)
				if(state.getSpace(i, j) == 0)
					return new Point(i,j);
		return null;
	}

	/**
	 * function that gets final move AI will make 
	 * @param state state of the game board
	 * @param deadline max time allowed in milliseconds
	 * @return move to be made
	 */
	@Override
	public Point getMove(BoardModel state, int deadline) {
		
		long start = System.currentTimeMillis();
		
		if (state.gravity)
		{
			Point result = alphaBetaDeepening(state, deadline, start);
			
			//Uncomment below for testing
//			long elapsedMillys = System.currentTimeMillis() - start;
//			float seconds = elapsedMillys / 1000f;
//			System.out.println("Time: " + seconds);
			
			return result;
		}
		
		else
		{
			Point result = gravOffMove(state, deadline, start);
			
			//Uncomment below for testing
//			long elapsedMillys = System.currentTimeMillis() - start;
//			float seconds = elapsedMillys / 1000f;
//			System.out.println("Time: " + seconds);

			return result;
		}
	}
}
