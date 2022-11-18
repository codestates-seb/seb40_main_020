import styled from 'styled-components';

export const TabComponent = styled.div`
	width: 100%;
	height: 100%;
	.title-menu {
		width: 100%;
		height: 45px;
		box-sizing: border-box;
		border-bottom: 1px solid var(--borderColor);
		display: flex;
		cursor: pointer;
		> div {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			font-weight: 700;
		}
		.select {
			color: var(--yellow);
			border-bottom: 3px solid var(--yellow);
		}
	}
`;
