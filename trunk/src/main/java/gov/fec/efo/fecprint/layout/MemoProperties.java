package gov.fec.efo.fecprint.layout;

import gov.fec.efo.fecprint.data.BaseRecordType;

public enum MemoProperties {
	
		F1(BaseRecordType.F1),		
		F1S(BaseRecordType.F1S),
		F1M(BaseRecordType.F1M),		
		F2(BaseRecordType.F2),		
		F2S(BaseRecordType.F2S),
		F24(BaseRecordType.F24),
		F3(BaseRecordType.F3),
		F3S(BaseRecordType.F3S),
		F3P(BaseRecordType.F3P),
		F3PS(BaseRecordType.F3PS),
		F3P31AL(BaseRecordType.F3P31AL,22,23),
		F3X(BaseRecordType.F3X),
		F3L(BaseRecordType.F3L),		
		F4(BaseRecordType.F4),
		F5(BaseRecordType.F5),
		F56(BaseRecordType.F56),
		F57(BaseRecordType.F57),
		F6(BaseRecordType.F6),
		F65(BaseRecordType.F65),
		F7(BaseRecordType.F7),
		F76(BaseRecordType.F76),
		F8(BaseRecordType.F8),
		F82(BaseRecordType.F82),
		F83(BaseRecordType.F83),
		F9(BaseRecordType.F9),		
		F91(BaseRecordType.F91),
		F92(BaseRecordType.F92),
		F93(BaseRecordType.F93),
		F13(BaseRecordType.F13),
		F132(BaseRecordType.F132, 21, 22),
		F133(BaseRecordType.F133, 20, 21),
		F99(BaseRecordType.F99, -1, 1),
		SA(BaseRecordType.SA, 43, 44),
		SA3L(BaseRecordType.SA3L, 43, 44),
		SB(BaseRecordType.SB, 42, 43),
		SB3L(BaseRecordType.SB3L, 42, 43),
		SC(BaseRecordType.SC, 37, 38),
		SC1(BaseRecordType.SC1), 
		SC1_3P(BaseRecordType.SC1_3P),
		SC2(BaseRecordType.SC2), 
		SD(BaseRecordType.SD), 
		SE(BaseRecordType.SE, 43, 44),
		SF(BaseRecordType.SF, 43, 44),
		H1(BaseRecordType.H1),
		H2(BaseRecordType.H2),
		H3(BaseRecordType.H3),
		H4(BaseRecordType.H4, 32, 33),
		H5(BaseRecordType.H5),
		H6(BaseRecordType.H6, 30, 31),
		SI(BaseRecordType.SI),
		SL(BaseRecordType.SL),
		SASL(BaseRecordType.SASL, 43, 44),
		SBSL(BaseRecordType.SBSL, 42, 43),
		F3Z1(BaseRecordType.F3Z1),
		F3Z2(BaseRecordType.F3Z2),
		F3PZ1(BaseRecordType.F3PZ1),
		F3PZ2(BaseRecordType.F3PZ2),
		TEXT(BaseRecordType.TEXT);
		
		private final BaseRecordType type;
		private final int positionMemoCode;
		private final int positionMemoText;
		
		MemoProperties(BaseRecordType t, int posMemoCode, int posMemoText)
		{
			type = t;
			positionMemoCode = posMemoCode;
			positionMemoText = posMemoText;
			
		}
		
		MemoProperties(BaseRecordType t)
		{
			type = t;
			positionMemoCode = -1;
			positionMemoText = -1;
			
		}

		public BaseRecordType getType() {
			return type;
		}

		public int getPositionMemoCode() {
			return positionMemoCode;
		}

		public int getPositionMemoText() {
			return positionMemoText;
		}

}
