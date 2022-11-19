import styled from 'styled-components';

export const Wrapper = styled.div`
	table thead th {
		width: 12%;
		:nth-child(1) {
			width: 150px;
		}
		:nth-child(2),
		:nth-child(3),
		:nth-child(4) {
			width: 100px;
		}
	}
`;
