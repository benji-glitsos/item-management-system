import { Fragment } from "react";
import NonBreakingSpace from "%/presenters/NonBreakingSpace";
import numbersToWords from "number-to-words";
import simplur from "simplur";
import capitaliseFirstLetter from "%/utilities/capitaliseFirstLetter";

export default ({
    isLoading,
    currentPage,
    pageLength,
    totalPages,
    itemRangeStart,
    itemRangeEnd,
    totalItemsCount,
    numberOfSelected
}) => {
    if (isLoading) {
        return <NonBreakingSpace />;
    } else if (totalItemsCount === undefined || totalItemsCount === null) {
        return "Zero items.";
    } else if (numberOfSelected === 0) {
        return (
            <Fragment>
                Showing items {itemRangeStart}
                &ndash;{itemRangeEnd} of total {totalItemsCount}.
            </Fragment>
        );
    } else {
        const numberToWorded = n =>
            capitaliseFirstLetter(numbersToWords.toWords(n));
        return (
            <Fragment>
                {simplur`${[
                    numberOfSelected,
                    numberToWorded
                ]} item[|s] selected.`}
            </Fragment>
        );
    }
};
