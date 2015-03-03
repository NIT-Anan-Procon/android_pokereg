package jp.ac.anan_nct.pokereg;


public enum KeySetPattern{
	TopDown, BottomUp;
	

	public Key getKeyFromIndex(int index){
		switch(this){
		case TopDown:
			switch(index){
			case 0: return Key.Number1;
			case 1: return Key.Number2;
			case 2: return Key.Number3;
			case 3: return Key.Number4;
			case 4: return Key.Number5;
			case 5: return Key.Number6;
			case 6: return Key.Number7;
			case 7: return Key.Number8;
			case 8: return Key.Number9;
			case 9: return Key.Number0;
			case 10: return Key.Enter;
			case 11: return Key.Clear;
			}
			break;
		case BottomUp:
			switch(index){
			case 0: return Key.Number7;
			case 1: return Key.Number8;
			case 2: return Key.Number9;
			case 3: return Key.Number4;
			case 4: return Key.Number5;
			case 5: return Key.Number6;
			case 6: return Key.Number1;
			case 7: return Key.Number2;
			case 8: return Key.Number3;
			case 9: return Key.Number0;
			case 10: return Key.Enter;
			case 11: return Key.Clear;
			}
			break;
		}
		return null;
	}

	public int getIndexFromKey(Key key){
		switch(this){
		case TopDown:
			switch(key){
			case Number1: return 0;
			case Number2: return 1;
			case Number3: return 2;
			case Number4: return 3;
			case Number5: return 4;
			case Number6: return 5;
			case Number7: return 6;
			case Number8: return 7;
			case Number9: return 8;
			case Number0: return 9;
			case Enter:   return 10;
			case Clear:   return 11;
			}
		case BottomUp:
			switch(key){
			case Number7: return 0;
			case Number8: return 1;
			case Number9: return 2;
			case Number4: return 3;
			case Number5: return 4;
			case Number6: return 5;
			case Number1: return 6;
			case Number2: return 7;
			case Number3: return 8;
			case Number0: return 9;
			case Enter:   return 10;
			case Clear:   return 11;
			}
		}
		return -1;
	}
}