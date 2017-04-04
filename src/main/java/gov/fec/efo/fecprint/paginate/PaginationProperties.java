package gov.fec.efo.fecprint.paginate;

import gov.fec.efo.fecprint.data.BaseRecordType;

public enum PaginationProperties {
	
		F1(BaseRecordType.F1, -1, 4),		
		F1S(BaseRecordType.F1S, 1),
		F1M(BaseRecordType.F1M, -1, 1),		
		F2(BaseRecordType.F2, -1, 1),		
		F2S(BaseRecordType.F2S, 3),
		F24(BaseRecordType.F24, -1, 1),
		F3(BaseRecordType.F3, -1, 4),
		F3S(BaseRecordType.F3S, -1, 4),
		F3P(BaseRecordType.F3P, -1, 7),
		F3PS(BaseRecordType.F3PS, -1, 6),
		F3P31AL(BaseRecordType.F3P31AL, 7),
		F3X(BaseRecordType.F3X, -1, 5),
		F3L(BaseRecordType.F3L, -1, 1),		
		F4(BaseRecordType.F4, -1, 2),
		F5(BaseRecordType.F5, -1, 1),
		F56(BaseRecordType.F56, 4),
		F57(BaseRecordType.F57, 3),
		F6(BaseRecordType.F6, -1, 1),
		F65(BaseRecordType.F65, 5),
		F7(BaseRecordType.F7, -1, 1),
		F76(BaseRecordType.F76, 5),
		F8(BaseRecordType.F8, -1, 1),
		F82(BaseRecordType.F82, 1),
		F83(BaseRecordType.F83, 5),
		F9(BaseRecordType.F9, -1, 1),		
		F91(BaseRecordType.F91, 5),
		F92(BaseRecordType.F92, 5),
		F93(BaseRecordType.F93, 2),
		F13(BaseRecordType.F13, -1, 1),
		F132(BaseRecordType.F132, 3),
		F133(BaseRecordType.F133, 3),
		F99(BaseRecordType.F99, -1, 1),
		SA(BaseRecordType.SA, 3),
		SA3L(BaseRecordType.SA3L, 4),
		SB(BaseRecordType.SB, 3),
		SB3L(BaseRecordType.SB3L, 5),
		SC(BaseRecordType.SC, 1),
		SC1(BaseRecordType.SC1, 1), 
		SC1_3P(BaseRecordType.SC1_3P, 1,2),
		SC2(BaseRecordType.SC2, 5), 
		SD(BaseRecordType.SD, 3), 
		SE(BaseRecordType.SE, 2),
		SF(BaseRecordType.SF, 3),
		H1(BaseRecordType.H1, 1),
		H2(BaseRecordType.H2, 6),
		H3(BaseRecordType.H3, 1),
		H4(BaseRecordType.H4, 3),
		H5(BaseRecordType.H5, 2),
		H6(BaseRecordType.H6, 3),
		SI(BaseRecordType.SI, 1),
		SL(BaseRecordType.SL, 1),
		SASL(BaseRecordType.SASL, 4),
		SBSL(BaseRecordType.SBSL, 5),
		F3Z1(BaseRecordType.F3Z1, 1),
		F3Z2(BaseRecordType.F3Z2, 1),
		F3PZ1(BaseRecordType.F3PZ1, 1),
		F3PZ2(BaseRecordType.F3PZ2, 1),
		TEXT(BaseRecordType.TEXT, 2);
		
		private final BaseRecordType type;
		private final int noOfRecordsPerPage;
		private final int pageSpan;
		
		PaginationProperties(BaseRecordType t, int recs)
		{
			type = t;
			noOfRecordsPerPage = recs;
			pageSpan = 1;
		}
		
		PaginationProperties(BaseRecordType t, int recs, int ps)
		{
			this.type = t;
			noOfRecordsPerPage = recs;
			pageSpan = ps;
		}

		public BaseRecordType getType() {
			return type;
		}

		public int getNoOfRecordsPerPage() {
			return noOfRecordsPerPage;
		}

		public int getPageSpan() {
			return pageSpan;
		}
}
