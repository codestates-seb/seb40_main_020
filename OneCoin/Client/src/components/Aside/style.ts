import styled from 'styled-components';

export const AsideComponent = styled.aside<{ isShow?: boolean }>`
	display: ${(props) => !props.isShow && 'none'};
	width: 100%;
	height: 100%;
	border: 1px solid var(--borderColor);
	background: #fff;
`;
