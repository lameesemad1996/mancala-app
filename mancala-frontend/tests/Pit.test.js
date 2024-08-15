import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import Pit from './../src/components/Pit';
import {expect, test, jest, beforeEach, describe} from "@jest/globals";
import { getPitByTestId } from "./TestUtils";

describe('Pit Component', () => {
    const handleClick = jest.fn();

    beforeEach(() => {
        handleClick.mockClear();
    });

    test('renders the correct number of stones', () => {
        render(<Pit stones={4} bigPit={false} clickable={true} onClick={handleClick} dataTestId="pit-0" />);
        expect(getPitByTestId(0)).toBeInTheDocument();
    });

    test('calls onClick when pit is clicked', () => {
        render(<Pit stones={4} bigPit={false} clickable={true} onClick={handleClick} dataTestId="pit-0" />);
        fireEvent.click(getPitByTestId(0));
        expect(handleClick).toHaveBeenCalledTimes(1);
    });

    test('does not call onClick when pit is not clickable', () => {
        render(<Pit stones={4} bigPit={false} clickable={false} onClick={handleClick} dataTestId="pit-0" />);
        fireEvent.click(getPitByTestId(0));
        expect(handleClick).not.toHaveBeenCalled();
    });
});
