import styled from 'styled-components';

export const ChartComponent = styled.div`
	width: 990px;
	height: 450px;
	#tradingview_widget_wrapper {
		> div {
			:nth-last-child(1) {
				display: none;
			}
		}
	}
	margin-bottom: 12px;
`;
